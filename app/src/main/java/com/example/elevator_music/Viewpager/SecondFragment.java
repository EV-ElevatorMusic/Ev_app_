package com.example.elevator_music.Viewpager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.elevator_music.R;

public class SecondFragment extends Fragment {
    private int frag_num;

    public SecondFragment(){
    }

    public static SecondFragment newInstance(int num){
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frag_num = getArguments().getInt("num",1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_second, container, false);
        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}