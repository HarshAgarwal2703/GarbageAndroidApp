package com.example.garbagecleanup.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.garbagecleanup.R;
import com.example.garbagecleanup.activity.LoginActivity;
import com.example.garbagecleanup.activity.MyIssuesActivity;
import com.example.garbagecleanup.activity.ViewDrafts;
import com.example.garbagecleanup.helper.AppConstants;
import com.example.garbagecleanup.helper.MySingleton;

import java.util.ArrayList;

public class TAB_3 extends Fragment {

    ArrayList ISSUELIST = new ArrayList<>();
    private Button btnMyIssues;
    private Button btnLogOut;
    private Button View_drafts;

    public static TAB_3 newInstance() {
        TAB_3 fragment = new TAB_3();
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab_3, container, false);
        View_drafts = view.findViewById(R.id.VIEW_BUTTON);
        btnLogOut = view.findViewById(R.id.btnLogOut);
        btnMyIssues = view.findViewById(R.id.btnMyIssues);
        btnMyIssues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(MyIssuesActivity.makeIntent(getActivity()));
            }
        });
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = MySingleton.getInstance().getSharedPreferences();
                sharedPreferences.edit().putBoolean(AppConstants.SP_LOGGED_IN, false).commit();
                sharedPreferences.edit().putString(AppConstants.SP_GET_USER, "").commit();
                Toast.makeText(getActivity(), "Logged Out", Toast.LENGTH_LONG).show();
                startActivity(LoginActivity.makeIntent(getActivity()));
                getActivity().finishAffinity();
            }
        });

        View_drafts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(ViewDrafts.makeIntent(getContext()));

            }
        });
        return view;

    }


}
