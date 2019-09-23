package com.example.garbagecleanup.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.garbagecleanup.AppConstants;
import com.example.garbagecleanup.R;
import com.example.garbagecleanup.activity.LoginActivity;
import com.example.garbagecleanup.activity.ViewDrafts;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;

public class TAB_3 extends Fragment {

    Button VIEW_ISSUE;
    private Button btnLogOut;
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
        btnLogOut = view.findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(AppConstants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
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
