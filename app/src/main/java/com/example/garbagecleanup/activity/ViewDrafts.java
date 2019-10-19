package com.example.garbagecleanup.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garbagecleanup.R;
import com.example.garbagecleanup.adapter.AutoFitGridLayoutManager;
import com.example.garbagecleanup.adapter.DraftsRecyclerAdapter;
import com.example.garbagecleanup.helper.AppConstants;
import com.example.garbagecleanup.helper.MySingleton;
import com.example.garbagecleanup.model.Draft;
import com.example.garbagecleanup.model.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ViewDrafts extends AppCompatActivity {

    private static final String TAG = "ViewDrafts";
    RecyclerView recyclerView;
    ArrayList<Draft> draftList;
    DraftsRecyclerAdapter adapter;

    public static Intent makeIntent(Context context) {
        return new Intent(context, ViewDrafts.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_drafts);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        draftList = MySingleton.getInstance(this).getAppDatabase().draftDAO().getAll();
        recyclerView.setHasFixedSize(true);

        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(this, 500);
        recyclerView.setLayoutManager(layoutManager);

        draftList = new ArrayList<>();
        adapter = new DraftsRecyclerAdapter(this, draftList);
        recyclerView.setAdapter(adapter);

//        draftList=getSample();


    }

    @Override
    protected void onResume() {
        super.onResume();
        draftList.clear();
        getDraftsFromDB();
        adapter.notifyDataSetChanged();
    }

    private void getDraftsFromDB() {

//        for(int i=0;i<5;i++)
//        {
//            Draft draft=new Draft(1,"djsjh","sdfmksdf","656565","dsfdf","jjbbk",new byte[5]);
//            draftList.add(draft);
//        }
        try {
            draftList.addAll(new GetDrafts(this).execute().get());
            adapter.notifyDataSetChanged();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getDraftsFromDB: " + draftList);

    }

    private static class GetDrafts extends AsyncTask<Void, Void, ArrayList<Draft>> {
        private Context context;

        public GetDrafts(Context context) {
            this.context = context;
        }

        @Override
        protected ArrayList<Draft> doInBackground(Void... voids) {
            User user = new Gson().fromJson(context.getSharedPreferences(AppConstants.SHARED_PREFERENCES_NAME, MODE_PRIVATE).getString(AppConstants.SP_GET_USER, ""), User.class);
            List<Draft> draftList = MySingleton.getInstance().getAppDatabase().draftDAO().getAllCurrentUserDrafts(user.getUserId());
//            Log.d(TAG, "doInBackground: "+draftList);
            return (ArrayList) draftList;
        }
    }

}
