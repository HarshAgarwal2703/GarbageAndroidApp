package com.example.garbagecleanup.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.garbagecleanup.R;
import com.example.garbagecleanup.helper.AppConstants;
import com.example.garbagecleanup.model.Issue_Model_Class;

import java.util.ArrayList;

/**
 * User: Aman
 * Date: 28-09-2019
 * Time: 12:26 AM
 */
public class MyIssuesRecyclerAdapter extends RecyclerView.Adapter<MyIssuesRecyclerAdapter.PageViewHolder> {

    private static final String TAG = "MyIssuesRecyclerAdapter";
    private Context context;
    private ArrayList<Issue_Model_Class> issueModelClassArrayList;

    public MyIssuesRecyclerAdapter(Context context, ArrayList<Issue_Model_Class> issueModelClassArrayList) {
        this.context = context;
        this.issueModelClassArrayList = issueModelClassArrayList;
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.issues_card_view, parent, false);
        return new PageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {

        final Issue_Model_Class issueModelClass = issueModelClassArrayList.get(position);
        Log.d(TAG, "onBindViewHolder: " + issueModelClass);
        holder.tvUpvotes.setText(String.valueOf(issueModelClass.getVotes()));
        holder.tvNoOfVotes.setVisibility(View.VISIBLE);
        holder.tvDescription.setText(issueModelClass.getDescription());
        holder.tvTitle.setText(issueModelClass.getTitle());
        holder.btnUpvote.setVisibility(View.GONE);
        holder.tvStatus.setText(issueModelClass.getStatus());
        holder.btnDelete.setVisibility(View.VISIBLE);
        Glide.with(context).load(issueModelClass.getImageUrl()).into(holder.ivImage);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.DELETE_POST + issueModelClass.getId() + "/", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(context, "POST DELETED", Toast.LENGTH_SHORT).show();
                        issueModelClassArrayList.remove(issueModelClass);
                        notifyDataSetChanged();
                    }
                },
                                                                new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Log.e(TAG, "onErrorResponse: " + error.toString() );
                            }
                        });

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                stringRequest.setShouldCache(false);
                requestQueue.add(stringRequest);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return issueModelClassArrayList.size();
    }

    class PageViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvDescription;
        private TextView tvUpvotes;
        private android.widget.ImageView ivImage;
        private ImageButton btnUpvote;
        private CardView cardView;
        private TextView tvStatus;
        private TextView tvNoOfVotes;
        private Button btnDelete;

        public PageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.TitleTextView);
            tvDescription = itemView.findViewById(R.id.DescriptionTextView);
            tvUpvotes = itemView.findViewById(R.id.UpvotesTextView);
            ivImage = itemView.findViewById(R.id.PostImageView);
            btnUpvote = itemView.findViewById(R.id.likeIcon);
            cardView = itemView.findViewById(R.id.CardView);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvNoOfVotes = itemView.findViewById(R.id.tvNoOfVotes);
            btnDelete = itemView.findViewById(R.id.deleteMyIssue);
        }
    }
}
