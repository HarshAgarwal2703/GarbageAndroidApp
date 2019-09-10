package com.example.garbagecleanup;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.garbagecleanup.Database.SampleSQLiteDBHelper;

import java.util.ArrayList;

public class TAB_3 extends Fragment {

    Button VIEW_ISSUE;
    SampleSQLiteDBHelper sampleSQLiteDBHelper;
    ArrayList ISSUELIST=new ArrayList<>();
    public static TAB_3 newInstance()
    {
        TAB_3 fragment = new TAB_3();
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab_3 , container , false);
        VIEW_ISSUE=view.findViewById(R.id.VIEW_BUTTON);
        sampleSQLiteDBHelper = new SampleSQLiteDBHelper(getContext());

        VIEW_ISSUE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readFromDB();
            }
        });
        return view;

    }

    public void readFromDB()
    {

        ISSUELIST=sampleSQLiteDBHelper.getRegistrationData();
        Log.e("cdscds",ISSUELIST.toString());
    }
}
