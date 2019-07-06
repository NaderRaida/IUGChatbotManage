package com.example.nadir.finalproject;


import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class ChatFragment extends Fragment {
    Button go_btn;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_features, container, false);

        go_btn = v.findViewById(R.id.go_btn);
        go_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent("com.iugaza.chatbot.ps.ACTION");
                    startActivity(intent);
                }catch(ActivityNotFoundException ex){
                    Toast.makeText(getActivity().getApplicationContext(), "لم تقم بتثبيت تطبيق الشات بوت", Toast.LENGTH_SHORT).show();
                }
            }
        });

         return v;
    }
}