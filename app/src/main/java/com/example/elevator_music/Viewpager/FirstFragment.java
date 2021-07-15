package com.example.elevator_music.Viewpager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.elevator_music.R;

public class FirstFragment extends Fragment {

    private int frag_num;

    public FirstFragment(){
    }

    public static FirstFragment newInstance(int num){
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frag_num = getArguments().getInt("num",0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_first, container, false);
        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}