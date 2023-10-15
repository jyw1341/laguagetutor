package com.jyw.laguagetutor.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.jyw.laguagetutor.StudentFragment;
import com.jyw.laguagetutor.TeacherFragment;

public class RecyclerViewPagerAdapter extends FragmentStateAdapter {


    public RecyclerViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 1 :
                return new StudentFragment();
        }

        return new TeacherFragment();
    }
}
