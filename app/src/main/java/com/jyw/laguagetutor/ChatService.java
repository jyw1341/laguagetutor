package com.jyw.laguagetutor;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.room.Room;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jyw.laguagetutor.adapter.ChatRoomDatabase;
import com.jyw.laguagetutor.recyclerData.ChatLogData;
import com.jyw.laguagetutor.recyclerData.ChatMessage;
import com.jyw.laguagetutor.recyclerData.ChatRoom;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatService extends Service {

    private String mMobileNumber,userName,currentRoomId;

    private static final String TAG = ChatService.class.getSimpleName();
    private static final String URL = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/get_chat_messages.php";
    private static final String URL_1 = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/get_image2.php";
    private static final String IMAGE_URL = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/images/";
    public static final String GROUP_ID = "CHAT_GROUP";

    private ChatClient chatClient;
    private Socket socket;
    private ChatRoomDatabase chatRoomDatabase;

    private final IBinder mBinder = new MyBinder();
    private Thread receiveMessage;

    private NotificationManagerCompat managerCompat;

    public ChatService() {

    }

    public class MyBinder extends Binder {
        public ChatService getService(){
            return ChatService.this;
        }
    }

    class NewRunnable implements Runnable{
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void run() {
            try {
//                chatRoomDatabase.chatLogDao().deleteAll();
//                chatRoomDatabase.chatRoomDao().deleteAll();
//                chatRoomDatabase.chatMessageDao().deleteAll();

                socket = new Socket("52.79.124.122", 1234);
                Log.i(TAG,"소켓 연결 성공");

                chatClient = new ChatClient(socket, mMobileNumber,userName);

                //로컬 DB 메세지 테이블에 저장된 메세지가 존재 -> 마지막으로 수신한 메세지의 시간 이후의 메세지를 서버에서 가져옴
                //로컬 DB 메세지 테이블에 저장된 메세지 없음 -> 접속한 아이디가 수신한 모든 메세지를 서버에서 가져옴
                if(chatRoomDatabase.chatLogDao().select(mMobileNumber)!=null){
                    String time = chatRoomDatabase.chatLogDao().getLog(mMobileNumber);
                    getMessages(time);
                    Log.e(TAG,time);
                } else {
                    Log.e(TAG,"dd");
                    updateChatLog(getLocalTime());
                }

                listenForMessage();

            } catch (IOException e) {
                Toast.makeText(getApplicationContext(),"서버 연결에 실패했습니다",Toast.LENGTH_SHORT).show();
                Log.i(TAG,"소켓 연결 실패");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetail();

        managerCompat = NotificationManagerCompat.from(this);
        if(mMobileNumber ==null){
            mMobileNumber = user.get(SessionManager.MOBILE_NUMBER);
        }
        if(userName==null){
            userName = user.get(SessionManager.USER_NAME);
        }

        if(chatRoomDatabase==null){
            chatRoomDatabase = Room.databaseBuilder(this,ChatRoomDatabase.class,"chat_room_db").fallbackToDestructiveMigration()
                    .build();
        }

        if(socket==null||chatClient==null){
            Thread socketConnectionThread  = new Thread( new NewRunnable(),"socketConnectionThread");
            socketConnectionThread.start();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //ChatActivity 에서 넘겨받음
        String command = intent.getStringExtra(getResources().getString(R.string.var_service_command));

        //ChatActivity 에서 넘겨받은 intent 로 조건문 실행
        if(command!=null){

            //채팅방 테이블에 새로운 채팅방 생성
            if(command.equals(getResources().getString(R.string.var_create_chat_room))){
                //선생님,학생 게시글에서 넘겨받음
                String roomId = intent.getStringExtra(getResources().getString(R.string.var_chat_room_id));

                if(roomId!=null){

                    Thread createChatRoom = new Thread("createChatRoom"){
                        @Override
                        public void run() {
                            String oMobileNumber = intent.getStringExtra(getResources().getString(R.string.other_mobile_number));
                            String participants = mMobileNumber +','+oMobileNumber;
                            String room_name = intent.getStringExtra(getResources().getString(R.string.var_room_name));

                            //채팅방 테이블에 저장
                            try{
                                chatRoomDatabase.chatRoomDao().insert(new ChatRoom(Integer.parseInt(roomId),mMobileNumber,participants,room_name,getResources().getString(R.string.var_private)));
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    };
                    createChatRoom.start();
                }

            //ChatActivity 에 채팅방 목록 전달
            } else if (command.equals(getResources().getString(R.string.var_get_room_data))){

                Thread getRoomList = new Thread("getRoomList"){
                    @Override
                    public void run() {

                        List<ChatRoom> roomList = chatRoomDatabase.chatRoomDao().getAll(mMobileNumber);

                        ArrayList<String> list = new ArrayList<>();
                        for (int i = 0; i < roomList.size(); i++){
                            list.add(roomList.get(i).toString().replace("[","").replace("]",""));
                        }

                        Intent sendRoomData = new Intent(getApplicationContext(), ChatActivity.class);
                        sendRoomData.putExtra(getResources().getString(R.string.var_activity_command),getResources().getString(R.string.var_create));
                        sendRoomData.putExtra(getResources().getString(R.string.var_chat_room_list),list);
                        sendRoomData.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(sendRoomData);
                    }
                };
                getRoomList.start();

            //ChatRoomActivity 에 채팅 내용 전달. 채팅방 메세지 초기화
            } else if(command.equals(getResources().getString(R.string.var_get_message_data))){

                Thread getMessageList = new Thread("getMessageList"){
                    @Override
                    public void run() {

                        String id = intent.getStringExtra(getResources().getString(R.string.var_chat_room_id));

                        //
                        currentRoomId = id;

                        managerCompat.cancel(Integer.parseInt(id));

                        String roomMember = chatRoomDatabase.chatRoomDao().getRoom(Integer.parseInt(id)).getRoom_people();
                        List<ChatMessage> messageList = chatRoomDatabase.chatMessageDao().getMessageByRoomId(id);

                        ArrayList<String> list = new ArrayList<>();
                        for (int i = 0; i < messageList.size(); i++){
                            list.add(messageList.get(i).toString().replace("[","").replace("]",""));
                        }

                        Intent sendMessageData = new Intent(getApplicationContext(), ChatRoomActivity.class);
                        sendMessageData.putExtra(getResources().getString(R.string.var_activity_command),getResources().getString(R.string.var_create));
                        sendMessageData.putExtra(getResources().getString(R.string.var_chat_message_list),list);
                        sendMessageData.putExtra(getResources().getString(R.string.var_room_members),roomMember);
                        sendMessageData.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(sendMessageData);

                        chatRoomDatabase.chatRoomDao().updateCheckedMessage(Integer.parseInt(id));
                    }
                };
                getMessageList.start();

            //ChatRoom 에서 넘겨받음
            } else if (command.equals(getResources().getString(R.string.var_send_message))){

                String message = intent.getStringExtra(getResources().getString(R.string.var_chat_message));
                String roomId = intent.getStringExtra(getResources().getString(R.string.var_chat_room_id));

                if(message!=null){
                    Thread sendMessageThread = new Thread("sendMessageThread"){
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void run() {

                            String formatDate = getLocalTime();

                            //내가 메시지를 보냈기 때문에 채탱방 목록 정보를 갱신한다
                            chatRoomDatabase.chatRoomDao().updateLastMessage(message,formatDate,Integer.parseInt(roomId));

                            //채팅방 멤버 데이터를 얻기 위해 채팅방 데이터 클래서 생성
                            ChatRoom chatRoom = chatRoomDatabase.chatRoomDao().getRoom(Integer.parseInt(roomId));

                            //로컬 DB에 메시지 저장
                            chatRoomDatabase.chatMessageDao().insert(new ChatMessage(roomId,mMobileNumber,userName,message,formatDate,mMobileNumber,chatRoom.getRoom_people(),getResources().getString(R.string.var_client),"2"));

                            updateChatLog(formatDate);

                            //내가 보낸 채팅은 바로 읽음 처리한다
                            chatRoomDatabase.chatRoomDao().updateCheckedMessage(Integer.parseInt(roomId));

                     

                            //서버로 채팅 메시지 전송
                            chatClient.sendMessage(roomId,getResources().getString(R.string.var_client),getResources().getString(R.string.var_send_message),message);
                        }
                    };
                    sendMessageThread.start();
                }

            } else if(command.equals(getResources().getString(R.string.var_check_message))){

                String roomId = intent.getStringExtra(getResources().getString(R.string.var_chat_room_id));
                Thread checkMessageThread = new Thread("checkMessageThread"){
                    @Override
                    public void run() {
                        super.run();
                        chatClient.sendMessage(roomId,getResources().getString(R.string.var_server),getResources().getString(R.string.var_check_message),"check_message");
                    }
                };
                checkMessageThread.start();

            //채팅방 데이터, 메세지 삭제
            } else if(command.equals(getResources().getString(R.string.var_delete))){

                String roomId = intent.getStringExtra(getResources().getString(R.string.var_chat_room_id));
                int int_id = Integer.parseInt(roomId);
                Thread deleteThread = new Thread("deleteThread"){
                    @Override
                    public void run() {
                        super.run();
                        chatRoomDatabase.chatRoomDao().delete(int_id);
                        chatRoomDatabase.chatMessageDao().delete(roomId);
                        Log.w(TAG,"delete done");
                    }
                };
                deleteThread.start();
            } else if(command.equals("buy_ticket")){
                String members = intent.getStringExtra("members");
                Thread getTicket = new Thread("getTicket"){
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void run() {
                        super.run();
                        int tmp = chatRoomDatabase.chatRoomDao().getRoomNumber(members);
                        String room = Integer.toString(tmp);

                        String message = "수강권 구매가 완료되었습니다";
                        String formatDate = getLocalTime();

                        //내가 메시지를 보냈기 때문에 채탱방 목록 정보를 갱신한다
                        chatRoomDatabase.chatRoomDao().updateLastMessage(message,formatDate,Integer.parseInt(room));

                        //채팅방 멤버 데이터를 얻기 위해 채팅방 데이터 클래서 생성
                        ChatRoom chatRoom = chatRoomDatabase.chatRoomDao().getRoom(Integer.parseInt(room));

                        //로컬 DB에 메시지 저장
                        chatRoomDatabase.chatMessageDao().insert(new ChatMessage(room,mMobileNumber,userName,message,formatDate,mMobileNumber,chatRoom.getRoom_people(),getResources().getString(R.string.var_client),"2"));

                        updateChatLog(formatDate);

                        //내가 보낸 채팅은 바로 읽음 처리한다
                        chatRoomDatabase.chatRoomDao().updateCheckedMessage(Integer.parseInt(room));

                        //서버로 채팅 메시지 전송
                        chatClient.sendMessage(room,getResources().getString(R.string.var_client),getResources().getString(R.string.var_send_message),message);

                        Log.w(TAG,"delete done");
                    }
                };
                getTicket.start();
            }
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            chatClient.closeEverything();
            Log.i(TAG,"소켓제거");
        } catch (Exception e){
            Log.i(TAG,"소켓닫기실패");
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }




    public void listenForMessage() {
        Thread listenForMessageThread = new Thread("listenForMessage"){
            @Override
            public void run() {
                String message;
                while (!socket.isClosed()) {
                    try {
                        Log.w(TAG,"listening");
                        message = chatClient.getBufferedReader().readLine();
                        queryMessage(message);

                    } catch (IOException e) {
                        Log.w(TAG,"listening end");
                        chatClient.closeEverything();
                        break;
                    }
                }
            }
        };
        listenForMessageThread.start();
    }

    //서버로부터 수신받은 메시지를 타입에 따라 처리하는 메소드
    private void queryMessage(String msg){
        try {
            JSONObject jsonObject = new JSONObject(msg);
            int room_id = Integer.parseInt(jsonObject.getString(getResources().getString(R.string.var_chat_room_id)));
            String id = jsonObject.getString(getResources().getString(R.string.var_chat_room_id));
            String otherUserMobileNumber = jsonObject.getString(getResources().getString(R.string.mobile_number));
            String user_name = jsonObject.getString(getResources().getString(R.string.var_username));
            String date = jsonObject.getString(getResources().getString(R.string.var_date));
            String chat_message = jsonObject.getString(getResources().getString(R.string.var_chat_message));
            String msgType = jsonObject.getString(getResources().getString(R.string.var_type));
            String task = jsonObject.getString(getResources().getString(R.string.var_task));

            if(msgType.equals(getResources().getString(R.string.var_server))){
                if (task.equals(getResources().getString(R.string.var_check_message))) {
                    Thread updateReaders = new Thread("updateReaders") {
                        @Override
                        public void run() {
                            super.run();

                            //메세지 읽은 사람 업데이트
                            chatRoomDatabase.chatMessageDao().updateReaders(',' + otherUserMobileNumber, id);

                            chatRoomDatabase.chatMessageDao().insert(new ChatMessage(id, otherUserMobileNumber, user_name, chat_message, date, null, null, msgType, null));

                            postToActivity(room_id,task,otherUserMobileNumber,user_name,chat_message);
                        }
                    };
                    updateReaders.start();
                }

            } else if(msgType.equals(getResources().getString(R.string.var_client))){
                if (task.equals(getResources().getString(R.string.var_send_message))) {
                    receiveMessage = new Thread("sendMessage") {
                        @Override
                        public void run() {
                            //나를 포함한 채팅방 멤버 변수
                            String participants = mMobileNumber + "," + otherUserMobileNumber;

                            //로컬 채팅방 테이블에 넘겨받은 roomId가 존재하지않는 경우 채팅방 생성

                            if (chatRoomDatabase.chatRoomDao().getRoom(room_id) == null) {
                                try{
                                    chatRoomDatabase.chatRoomDao().insert(new ChatRoom(room_id, mMobileNumber,participants, user_name, getResources().getString(R.string.var_private)));

                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }

                            // 새로받은 메시지를 기반으로 채팅방에 표시되는 메시지, 시간 업데이트
                            chatRoomDatabase.chatRoomDao().updateLastMessage(chat_message, date, room_id);

                            //채팅방 멤버 데이터를 얻기 위해 채팅방 데이터 클래서 생성
                            ChatRoom chatRoom = chatRoomDatabase.chatRoomDao().getRoom(room_id);

                            //메시지 테이블에 메시지 저장
                            chatRoomDatabase.chatMessageDao().insert(new ChatMessage(id, otherUserMobileNumber, user_name, chat_message, date, participants, chatRoom.getRoom_people(), msgType, "1"));

                            updateChatLog(date);

                            postToActivity(room_id,task,otherUserMobileNumber,user_name,chat_message);


                        }
                    };
                    receiveMessage.start();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getLocalTime(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private void updateChatLog(String formatDate){
        if(chatRoomDatabase.chatLogDao().select(mMobileNumber)==null){
            chatRoomDatabase.chatLogDao().insert(new ChatLogData(mMobileNumber,formatDate));
            Log.d(TAG,formatDate);
        } else {
            chatRoomDatabase.chatLogDao().update(mMobileNumber,formatDate);
            Log.d(TAG,formatDate);
        }
    }

    private void sendOnChannel1(String sender, String message,int roomId,String image){
        Bitmap bitmap = stringToBitmap(image);

        Intent intent = new Intent(this, ChatRoomActivity.class);
        intent.putExtra(getResources().getString(R.string.var_chat_room_id),Integer.toString(roomId));
        intent.putExtra("bitmap",image);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, roomId, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Notification notification = new NotificationCompat.Builder(this,NotiChannel.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_android_black_24dp)
                .setContentTitle(sender)
                .setContentText(message)
                .setLargeIcon(bitmap)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .setGroup(GROUP_ID)
                .setAutoCancel(true)
                .build();

        managerCompat.notify(roomId,notification);
        summaryNotification();
    }

    private void summaryNotification(){
        Notification notification = new NotificationCompat.Builder(this,NotiChannel.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_android_black_24dp)
                .setGroup(GROUP_ID)
                .setGroupSummary(true)
                .setAutoCancel(true)
                .build();
        managerCompat.notify(100000,notification);
    }


    //사용자의 현재 화면이 ChatActivity or ChatRoomActivity 일 경우 실시간 화면 업데이트를 처리하는 메소드
    public void postToActivity(int roomId, String task, String otherUserMobileNumber,String user_name,String chat_message){

        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> info = manager.getRunningTasks(1);
        ComponentName componentName= info.get(0).topActivity;
        String topActivityName = componentName.getShortClassName().substring(1);

            //현재 최상단 액티비티 ChatActivity (채팅방목록)
            if(topActivityName.equals(ChatActivity.class.getSimpleName())){

                //채팅방 리사이클러뷰 업데이트
                if(task.equals(getResources().getString(R.string.var_send_message))){
                    ChatRoom chatRoom = chatRoomDatabase.chatRoomDao().getRoom(roomId);
                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                    intent.putExtra(getResources().getString(R.string.var_activity_command),getResources().getString(R.string.var_update));
                    intent.putExtra(getResources().getString(R.string.var_chat_room_data),chatRoom.toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    //알림 생성
                    volley(otherUserMobileNumber,user_name,chat_message,roomId);
                }

            //현재 최상단 액티비티 ChatRoomActivity (채팅 화면)
            } else if(topActivityName.equals(ChatRoomActivity.class.getSimpleName())){

                //task = send_message -> 채팅화면 리사이클러뷰 업데이트
                if(task.equals(getResources().getString(R.string.var_send_message))){

                    //현재 채팅방 != 메세지 채팅방 -> 알림만
                    if(!currentRoomId.equals(Integer.toString(roomId))){
                        volley(otherUserMobileNumber,user_name,chat_message,roomId);
                    } else {
                    //현재 채팅방 id = 수신한 메세지 채팅방id -> 알림x 메세지 업데이트 o
                        ChatMessage chatMessage = chatRoomDatabase.chatMessageDao().getMessage();
                        Intent intent = new Intent(getApplicationContext(), ChatRoomActivity.class);
                        intent.putExtra(getResources().getString(R.string.var_activity_command),getResources().getString(R.string.var_update));
                        intent.putExtra(getResources().getString(R.string.var_chat_data),chatMessage.toString());

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        //채팅방 정보 업데이트
                        chatRoomDatabase.chatRoomDao().updateCheckedMessage(roomId);
                    }

                //task = check_message -> 상대가 메세지 읽었을 경우 읽음처리
                } else if(task.equals(getResources().getString(R.string.var_check_message))){
                    //현재 채팅방 id = 수신한 메세지 채팅방 id -> 읽음처리
                    if(currentRoomId.equals(Integer.toString(roomId))){
                        Intent intent = new Intent(getApplicationContext(), ChatRoomActivity.class);
                        intent.putExtra(getResources().getString(R.string.var_activity_command), getResources().getString(R.string.var_check_message));
                        intent.putExtra(getResources().getString(R.string.mobile_number), otherUserMobileNumber);

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }

            //채팅방리스트, 채팅방 이외의 화면일때 메시지 수신시 알림 생성
            } else if(task.equals(getResources().getString(R.string.var_send_message))){
                volley(otherUserMobileNumber,user_name,chat_message,roomId);
            }
    }

        private void volley(String otherUserMobileNumber,String user_name,String chat_message,int roomId) {
                StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            sendOnChannel1(user_name,chat_message,roomId,response);
                        } catch (Exception e){
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("mobile_number",otherUserMobileNumber);
                    return data;
                }
            };
            request.setShouldCache(false);
            Volley.newRequestQueue(this).add(request);
        }

        //로컬 DB 메세지 테이블에 저장된 마지막 메세지 이후로 받은 메세지를 서버에서 가져온다
        private void getMessages(String lastMessageDate) {
                StringRequest request = new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String condition = jsonObject.getString(getResources().getString(R.string.condition));
                            if(condition.equals(getResources().getString(R.string.success))){
                                JSONArray jsonArray = jsonObject.getJSONArray(getResources().getString(R.string.var_message_array));

                                Thread query = new Thread(){
                                    @Override
                                    public void run() {
                                        super.run();
                                        for (int i = 0; i < jsonArray.length(); i++){
                                            try {
                                                queryMessage(jsonArray.get(i).toString());
                                                Log.e(TAG,jsonArray.get(i).toString());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            try {
                                                if(receiveMessage!=null){
                                                    receiveMessage.join();
                                                }
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                };

                                query.start();
                            } else {
                                Log.w(TAG,jsonObject.getString(getResources().getString(R.string.var_error)));
                            }

                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put(getResources().getString(R.string.mobile_number),mMobileNumber);
                    data.put(getResources().getString(R.string.var_last_message_date),lastMessageDate);
                    return data;
                }
            };
            request.setShouldCache(false);
            Volley.newRequestQueue(this).add(request);
        }

    private Bitmap stringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}