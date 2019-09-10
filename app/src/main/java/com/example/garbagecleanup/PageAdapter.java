package com.example.garbagecleanup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PageAdapter extends RecyclerView.Adapter<PageAdapter.PageViewHolder>{


    private Context context;
    private List<Issue_Model_Class> issueList;

    public PageAdapter(Context context, List<Issue_Model_Class> issueList) {
        this.context = context;
        this.issueList = issueList;
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.issues_card_view,null);
        PageViewHolder pageViewHolder=new PageViewHolder(view);


        return pageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {

        Issue_Model_Class issueModelClass = issueList.get(position);
        holder.UpvotesTextView.setText(String.valueOf(issueModelClass.getRating()));
        holder.LatitudeTextView.setText(issueModelClass.getLatitude());
        holder.LongitudeTextView.setText(issueModelClass.getLongitude());

   //     holder.ImageView.setImageDrawable(context.getResources().getDrawable(issueModelClass.getImg(),null));
    }

    @Override
    public int getItemCount() {
        return issueList.size();
    }

    class PageViewHolder extends RecyclerView.ViewHolder {

        TextView LatitudeTextView;
        TextView LongitudeTextView;
        TextView UpvotesTextView;
        ImageView ImageView;



        public PageViewHolder(@NonNull View itemView) {
            super(itemView);
            LatitudeTextView=itemView.findViewById(R.id.LatitudeTextView);
            LongitudeTextView=itemView.findViewById(R.id.LongitudeTextView);
            UpvotesTextView=itemView.findViewById(R.id.UpvotesTextView);
            ImageView=itemView.findViewById(R.id.imageView);
        }
    }
}
