package com.example.garbagecleanup.adapter;

import android.annotation.SuppressLint;
import android.app.AppComponentFactory;
import android.app.DownloadManager;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.garbagecleanup.PrefManager;
import com.example.garbagecleanup.R;
import com.example.garbagecleanup.helper.AppConstants;
import com.example.garbagecleanup.helper.MySingleton;
import com.example.garbagecleanup.model.Issue_Model_Class;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.PageViewHolder> {

    private static final String TAG = "FeedRecyclerAdapter";
    private Context context;
    private List<Issue_Model_Class> issueList;

    public FeedRecyclerAdapter(Context context, List<Issue_Model_Class> issueList) {
        this.context = context;
        this.issueList = issueList;
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.issues_card_view, parent, false);
        PageViewHolder pageViewHolder = new PageViewHolder(view);
        return pageViewHolder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final PageViewHolder holder, int position) {
         final Issue_Model_Class issueModelClass = issueList.get(position);
        holder.UpvotesTextView.setText(String.valueOf(issueModelClass.getVotes()));
        holder.descriptionTexView.setText(issueModelClass.getDescription());
        holder.TitleTextView.setText(issueModelClass.getTitle());
        holder.AreaTexView.setText(MySingleton.getAdress(Double.parseDouble(issueModelClass.getLatitude()),Double.parseDouble(issueModelClass.getLongitude())));
        if(issueModelClass.isCheckLiked()){
            holder.UpVoteLikeButton.setImageResource(R.drawable.ic_thumb_up_green_24dp);
        }else{
            holder.UpVoteLikeButton.setImageResource(R.drawable.ic_thumb_up_black_24dp);
        }
//        holder.ImageView.setImageDrawable(Glide.get(context).);
        Glide.with(context).load(issueModelClass.getImageUrl()).into(holder.ImageView);
        holder.cardView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    if (!issueModelClass.isCheckLiked()) {
                        holder.UpVoteLikeButton.setImageResource(R.drawable.ic_thumb_up_green_24dp);
                        issueModelClass.setCheckLiked(true);
                        issueModelClass.setVotes((int) (issueModelClass.getVotes() + 1));
                        holder.UpvotesTextView.setText(String.valueOf(issueModelClass.getVotes()));
                        likePost(issueModelClass.getId(), PrefManager.getUser().getUserId(),holder);

                    } else {
                        holder.UpVoteLikeButton.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                        issueModelClass.setCheckLiked(false);
                        issueModelClass.setVotes((int) (issueModelClass.getVotes() - 1));
                        holder.UpvotesTextView.setText(String.valueOf(issueModelClass.getVotes()));

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
                    holder.UpVoteLikeButton.setImageResource(R.drawable.ic_thumb_up_green_24dp);
                    issueModelClass.setCheckLiked(true);
                    issueModelClass.setVotes((int) (issueModelClass.getVotes() + 1));
                    holder.UpvotesTextView.setText(String.valueOf(issueModelClass.getVotes()));
                    likePost(issueModelClass.getId(), PrefManager.getUser().getUserId(),holder);
                } else {
                    holder.UpVoteLikeButton.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                    issueModelClass.setCheckLiked(false);
                    issueModelClass.setVotes((int) (issueModelClass.getVotes() - 1));
                    holder.UpvotesTextView.setText(String.valueOf(issueModelClass.getVotes()));

                }
            }
        });
    }

    private void likePost(int PostId, int UserId, final PageViewHolder holder) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", UserId);
            jsonObject.put("post_id", PostId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppConstants.UPVOTE_POST, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, "onResponse: " + response.toString());
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: " + error.toString());
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }


    @Override
    public int getItemCount() {
        return issueList.size();
    }

    class PageViewHolder extends RecyclerView.ViewHolder {

        TextView TitleTextView;
        TextView descriptionTexView;
        TextView AreaTexView;
        TextView UpvotesTextView;
        ImageView ImageView;
        ImageButton UpVoteLikeButton;
        CardView cardView;

        public PageViewHolder(@NonNull View itemView) {
            super(itemView);
            TitleTextView = itemView.findViewById(R.id.TitleTextView);
            descriptionTexView = itemView.findViewById(R.id.DescriptionTextView);
            UpvotesTextView = itemView.findViewById(R.id.UpvotesTextView);
            ImageView = itemView.findViewById(R.id.PostImageView);
            UpVoteLikeButton = itemView.findViewById(R.id.likeIcon);
            cardView = itemView.findViewById(R.id.CardView);
            AreaTexView = itemView.findViewById(R.id.AreaTextView);

        }
    }
}
