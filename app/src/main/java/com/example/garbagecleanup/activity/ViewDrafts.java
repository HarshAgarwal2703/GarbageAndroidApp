package com.example.garbagecleanup.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.garbagecleanup.R;
import com.example.garbagecleanup.adapter.AutoFitGridLayoutManager;
import com.example.garbagecleanup.adapter.DraftsRecyclerAdapter;
import com.example.garbagecleanup.model.Draft;

import java.util.ArrayList;
import java.util.List;

public class ViewDrafts extends AppCompatActivity {


    RecyclerView recyclerView;
    ArrayList<Draft> draftList;
    DraftsRecyclerAdapter adapter;
    private static final String TAG = "ViewDrafts";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_drafts);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        draftList = MySingleton.getInstance(this).getAppDatabase().draftDAO().getAll();
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        draftList=new ArrayList<>();
        adapter = new DraftsRecyclerAdapter(this, draftList);
        recyclerView.setAdapter(adapter);
        for(int i=0;i<5;i++)
        {
            Draft draft=new Draft(1,"djsjh","sdfmksdf","656565","dsfdf","jjbbk",new byte[5]);
            draftList.add(draft);
            Log.i(TAG, "onCreate: "+draftList);
        }

//        draftList=getSample();
        adapter.notifyDataSetChanged();

    }

    ArrayList<Draft> getSample() {


        for(int i=0;i<5;i++)
        {
            Draft draft=new Draft(1,"djsjh","sdfmksdf","656565","dsfdf","jjbbk",new byte[5]);
            draftList.add(draft);
        }
        adapter.notifyDataSetChanged();
        return draftList;

    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ViewDrafts.class);
    }


}
