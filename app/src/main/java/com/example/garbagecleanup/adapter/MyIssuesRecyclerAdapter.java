package com.example.garbagecleanup.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.garbagecleanup.R;
import com.example.garbagecleanup.model.Issue_Model_Class;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

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

        Issue_Model_Class issueModelClass = issueModelClassArrayList.get(position);
        Log.d(TAG, "onBindViewHolder: " + issueModelClass);
        holder.tvUpvotes.setText(String.valueOf(issueModelClass.getVotes()));
        holder.tvNoOfVotes.setVisibility(View.VISIBLE);
        holder.tvDescription.setText(issueModelClass.getDescription());
        holder.tvTitle.setText(issueModelClass.getTitle());
        holder.btnUpvote.setVisibility(View.GONE);
        holder.tvStatus.setText(issueModelClass.getStatus());
        Glide.with(context).load(issueModelClass.getImageUrl()).into(holder.ivImage);
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
        }
    }
}
