package com.example.grabagecleanup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PageAdapter extends RecyclerView.Adapter<PageAdapter.PageViewHolder>{


    private Context context;
    private List<Model> issueList;

    public PageAdapter(Context context, List<Model> issueList) {
        this.context = context;
        this.issueList = issueList;
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.fragment_tab_1,null);
        PageViewHolder pageViewHolder=new PageViewHolder(view);


        return pageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {

        Model model= issueList.get(position);
        holder.UpvotesTextView.setText(String.valueOf(model.getRating()));
        holder.LongitudeTextView.setText(model.getLatitude());
        holder.LongitudeTextView.setText(model.getLongitude());

        holder.ImageView.setImageDrawable(context.getResources().getDrawable(model.getImg(),null));
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
