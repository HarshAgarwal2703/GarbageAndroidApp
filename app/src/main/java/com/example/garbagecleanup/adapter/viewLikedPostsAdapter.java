package com.example.garbagecleanup.adapter;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.garbagecleanup.PrefManager;
import com.example.garbagecleanup.R;
import com.example.garbagecleanup.helper.AppConstants;
import com.example.garbagecleanup.model.Issue_Model_Class;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * User: Aman
 * Date: 28-09-2019
 * Time: 12:26 AM
 */
public class viewLikedPostsAdapter extends RecyclerView.Adapter<viewLikedPostsAdapter.PageViewHolder> {

    private static final String TAG = "MyIssuesRecyclerAdapter";
    private Context context;
    private ArrayList<Issue_Model_Class> issueModelClassArrayList;

    public viewLikedPostsAdapter(Context context, ArrayList<Issue_Model_Class> issueModelClassArrayList) {
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
    public void onBindViewHolder(@NonNull final PageViewHolder holder, int position) {

        final Issue_Model_Class issueModelClass = issueModelClassArrayList.get(position);
        Log.d(TAG, "onBindViewHolder: " + issueModelClass);
        holder.tvUpvotes.setText(String.valueOf(issueModelClass.getVotes()));
        holder.tvNoOfVotes.setVisibility(View.GONE);
        holder.tvDescription.setText(issueModelClass.getDescription());
        holder.tvTitle.setText(issueModelClass.getTitle());
        issueModelClass.setCheckLiked(true);
        holder.btnUpvote.setImageResource(R.drawable.ic_thumb_up_green_24dp);
        holder.tvStatus.setText(issueModelClass.getStatus());
        holder.btnDelete.setVisibility(View.GONE);
        Glide.with(context).load(issueModelClass.getImageUrl()).into(holder.ivImage);


        holder.cardView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    if (!issueModelClass.isCheckLiked()) {
                        holder.btnUpvote.setImageResource(R.drawable.ic_thumb_up_green_24dp);
                        issueModelClass.setCheckLiked(true);
                        issueModelClass.setVotes((int) (issueModelClass.getVotes() + 1));
                        holder.tvUpvotes.setText(String.valueOf(issueModelClass.getVotes()));
                        likePost(issueModelClass.getId(), PrefManager.getUser().getUserId(), holder);

                    } else {
                        holder.btnUpvote.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                        issueModelClass.setCheckLiked(false);
                        issueModelClass.setVotes((int) (issueModelClass.getVotes() - 1));
                        holder.tvUpvotes.setText(String.valueOf(issueModelClass.getVotes()));
                        dislikePost(issueModelClass.getId(), PrefManager.getUser().getUserId(), holder);
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

        holder.btnUpvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!issueModelClass.isCheckLiked()) {
                    holder.btnUpvote.setImageResource(R.drawable.ic_thumb_up_green_24dp);
                    issueModelClass.setCheckLiked(true);
                    issueModelClass.setVotes((int) (issueModelClass.getVotes() + 1));
                    holder.tvUpvotes.setText(String.valueOf(issueModelClass.getVotes()));
                    likePost(issueModelClass.getId(), PrefManager.getUser().getUserId(), holder);
                } else {
                    holder.btnUpvote.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                    issueModelClass.setCheckLiked(false);
                    issueModelClass.setVotes((int) (issueModelClass.getVotes() - 1));
                    holder.tvUpvotes.setText(String.valueOf(issueModelClass.getVotes()));
                    dislikePost(issueModelClass.getId(), PrefManager.getUser().getUserId(), holder);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return issueModelClassArrayList.size();
    }

    private void likePost(int PostId, int UserId, final viewLikedPostsAdapter.PageViewHolder holder) {

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
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }

    private void dislikePost(int PostId, int UserId, final viewLikedPostsAdapter.PageViewHolder holder) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", UserId);
            jsonObject.put("post_id", PostId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppConstants.DOWNVOTE_POST, jsonObject, new Response.Listener<JSONObject>() {
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
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
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
