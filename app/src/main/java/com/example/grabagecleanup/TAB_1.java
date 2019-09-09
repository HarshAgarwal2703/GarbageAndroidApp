package com.example.grabagecleanup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TAB_1 extends Fragment {

    RecyclerView recyclerView;
    PageAdapter pageAdapter;
    List<Model> IssueList;
    public static TAB_1 newInstance()
    {
        TAB_1 fragment = new TAB_1();
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab_1 , container , false);
        IssueList=new ArrayList<>();
        recyclerView= view.findViewById(R.id.RecyclerTab1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        pageAdapter=new PageAdapter(getContext(),IssueList);
        recyclerView.setAdapter(pageAdapter);

        return view;
    }
}
