package com.example.elevator_music.Viewpager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.elevator_music.EnterActivity;
import com.example.elevator_music.R;

public class ThirdFragment extends Fragment {
    private int frag_num;
    Button viewpager_btn;

    public ThirdFragment(){
    }

    public static ThirdFragment newInstance(int num){
        ThirdFragment fragment = new ThirdFragment();
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
        View v = inflater.inflate(R.layout.fragment_third, container, false);
        viewpager_btn = v.findViewById(R.id.viewpager_btn);
        viewpager_btn.setOnClickListener(v1 -> {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirst", false);
            editor.apply();

            Intent intent = new Intent(getContext(), EnterActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}