package com.example.elevator_music.Viewpager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

public class ViewpagerAdapter extends FragmentStateAdapter {
    public int mCount;

    public ViewpagerAdapter(FragmentActivity fa, int count){
        super(fa);
        mCount = count;
    }

    @NotNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);

        switch(index){
            case 0 :
                return FirstFragment.newInstance(index+1);
            case 1:
                return SecondFragment.newInstance(index+1);
            case 2:
                return ThirdFragment.newInstance(index+1);
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public int getRealPosition(int position) { return position % mCount; }



}
