package com.example.garbagecleanup.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.garbagecleanup.R;
import com.example.garbagecleanup.activity.ViewDrafts;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TAB_3 extends Fragment {

    Button View_drafts;
    ArrayList ISSUELIST = new ArrayList<>();

    public static TAB_3 newInstance() {
        TAB_3 fragment = new TAB_3();
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab_3, container, false);
        View_drafts = view.findViewById(R.id.VIEW_BUTTON);

        View_drafts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(ViewDrafts.makeIntent(getContext()));

            }
        });
        return view;

    }


}
