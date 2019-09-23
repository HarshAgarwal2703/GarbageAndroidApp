package com.example.garbagecleanup.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.garbagecleanup.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TAB_3 extends Fragment {

    Button VIEW_ISSUE;
    ArrayList ISSUELIST = new ArrayList<>();

    public static TAB_3 newInstance() {
        TAB_3 fragment = new TAB_3();
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab_3, container, false);
        VIEW_ISSUE = view.findViewById(R.id.VIEW_BUTTON);


        VIEW_ISSUE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                readFromDB();
            }
        });
        return view;

    }


}
