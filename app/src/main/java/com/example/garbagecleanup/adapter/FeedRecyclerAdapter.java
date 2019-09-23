package com.example.garbagecleanup.adapter;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.garbagecleanup.R;
import com.example.garbagecleanup.model.Issue_Model_Class;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.PageViewHolder> {

    private Context context;
    private List<Issue_Model_Class> issueList;
    Issue_Model_Class issueModelClass;
    int count = 0;

    public FeedRecyclerAdapter(Context context, List<Issue_Model_Class> issueList) {
        this.context = context;
        this.issueList = issueList;
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.issues_card_view, null);
        PageViewHolder pageViewHolder = new PageViewHolder(view);
        Log.e("jdbkkjsb", String.valueOf(count++));
        return pageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PageViewHolder holder, int position) {
        issueModelClass = issueList.get(position);
        holder.UpvotesTextView.setText(String.valueOf(issueModelClass.getRating()));
        holder.LatitudeTextView.setText(issueModelClass.getLatitude());
        holder.LongitudeTextView.setText(issueModelClass.getLongitude());
        holder.UpVoteLikeButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);

        holder.cardView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    Log.d("TEST", "onDoubleTap");
                    if (!issueModelClass.isCheckLiked()) {
                        holder.UpVoteLikeButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                        issueModelClass.setCheckLiked(true);
                        issueModelClass.setRating((int) (issueModelClass.getRating() + 1));
                        holder.UpvotesTextView.setText(String.valueOf(issueModelClass.getRating()));
                    } else {
                        holder.UpVoteLikeButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        issueModelClass.setCheckLiked(false);
                        issueModelClass.setRating((int) (issueModelClass.getRating() - 1));
                        holder.UpvotesTextView.setText(String.valueOf(issueModelClass.getRating()));

                    }
                    return super.onDoubleTap(e);
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent event) {
                    Log.d("TEST", "onSingleTap");
                    return false;
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                gestureDetector.onTouchEvent(event);
                return true;
            }

        });

        holder.UpVoteLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!issueModelClass.isCheckLiked()) {
                    holder.UpVoteLikeButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                    issueModelClass.setCheckLiked(true);
                    issueModelClass.setRating((int) (issueModelClass.getRating() + 1));
                    holder.UpvotesTextView.setText(String.valueOf(issueModelClass.getRating()));
                } else {
                    holder.UpVoteLikeButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    issueModelClass.setCheckLiked(false);
                    issueModelClass.setRating((int) (issueModelClass.getRating() - 1));
                    holder.UpvotesTextView.setText(String.valueOf(issueModelClass.getRating()));

                }
            }
        });
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
        ImageButton UpVoteLikeButton;
        CardView cardView;

        public PageViewHolder(@NonNull View itemView) {
            super(itemView);
            LatitudeTextView = itemView.findViewById(R.id.TitleTextView);
            LongitudeTextView = itemView.findViewById(R.id.DescriptionTextView);
            UpvotesTextView = itemView.findViewById(R.id.UpvotesTextView);
            ImageView = itemView.findViewById(R.id.PostImageView);
            UpVoteLikeButton = itemView.findViewById(R.id.likeIcon);
            cardView = itemView.findViewById(R.id.CardView);
        }
    }
}
