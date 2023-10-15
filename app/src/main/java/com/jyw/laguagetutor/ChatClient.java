package com.jyw.laguagetutor;

import android.util.Log;

import com.google.gson.Gson;
import com.jyw.laguagetutor.recyclerData.SendMessage;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class ChatClient {

    private Socket socket;
    private String mobileNumber;
    private String userName;
    private static final String TAG = ChatClient.class.getSimpleName();
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public ChatClient(Socket socket,String mobileNumber, String userName) {
        try {
            this.socket = socket;
            this.mobileNumber = mobileNumber;
            this.userName = userName;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            bufferedWriter.write(mobileNumber);
            bufferedWriter.newLine();
            bufferedWriter.flush();

        } catch (IOException e) {
            closeEverything();
        }
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }

    // Sending a message isn't blocking and can be done without spawning a thread, unlike waiting for a message.
    public void sendMessage(String roomId,String messageType, String task, String messageToSend) {
        try {
            Gson gson = new Gson();
            SendMessage sendMessage = new SendMessage(roomId,messageType,task,mobileNumber,userName,messageToSend);
            String messageJson = gson.toJson(sendMessage);

            bufferedWriter.write(messageJson);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            // Gracefully close everything.
            closeEverything();
        }
    }

    public void listenForMessage() {

        Thread listenForMessageThread = new Thread("listenForMessage"){
            @Override
            public void run() {
                String msgFromGroupChat;
                while (!socket.isClosed()) {
                    try {
                        Log.w(TAG,"listening");
                        msgFromGroupChat = bufferedReader.readLine();
                    } catch (IOException e) {
                        Log.w(TAG,"listening end");
                        closeEverything();
                        break;
                    }
                }
            }
        };
        listenForMessageThread.start();
    }

    // Helper method to close everything so you don't have to repeat yourself.
    public void closeEverything() {
        // Note you only need to close the outer wrapper as the underlying streams are closed when you close the wrapper.
        // Note you want to close the outermost wrapper so that everything gets flushed.
        // Note that closing a socket will also close the socket's InputStream and OutputStream.
        // Closing the input stream closes the socket. You need to use shutdownInput() on socket to just close the input stream.
        // Closing the socket will also close the socket's input stream and output stream.
        // Close the socket after closing the streams.
        try {
            if (socket != null) {
                socket.close();
                socket = null;
                Log.i(TAG,"socket closed");
            }

            if (bufferedReader != null) {
                bufferedReader.close();
                bufferedReader = null;
                Log.i(TAG,"bufferedReader closed");
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
                bufferedWriter = null;
                Log.i(TAG,"bufferedWriter closed");
            }

        } catch (IOException e) {
            Log.i(TAG,"closeEverything error");
            e.printStackTrace();
        }
    }
}