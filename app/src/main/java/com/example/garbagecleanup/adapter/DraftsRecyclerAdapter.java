package com.example.garbagecleanup.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.garbagecleanup.R;
import com.example.garbagecleanup.activity.DisplayImages;
import com.example.garbagecleanup.helper.MySingleton;
import com.example.garbagecleanup.model.Draft;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class DraftsRecyclerAdapter extends RecyclerView.Adapter<DraftsRecyclerAdapter.DraftsViewHolder> {

    private Context context;
    private List<Draft> DraftList;
    Draft draft;
    int count = 0;
    private static final String TAG = "DraftsRecyclerAdapter";

    public DraftsRecyclerAdapter(Context context, List<Draft> draftList) {
        this.context = context;
        this.DraftList = draftList;
        Log.i(TAG, "DraftsRecyclerAdapter: " + draftList);
    }

    @NonNull
    @Override
    public DraftsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.drafts_card_view, null);
        DraftsViewHolder draftsViewHolder = new DraftsViewHolder(view);
        Log.e("jdbkkjsb", String.valueOf(count++));
        return draftsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final DraftsViewHolder holder, int position) {

        draft = DraftList.get(position);
        Log.i(TAG, "onBindViewHolder: " + draft);
        holder.TitleTextView.setText(draft.getTitle());
        holder.DescriptionTextView.setText(draft.getDescription());
        holder.AreaTextView.setText(draft.getAreaName());
        holder.TimeStamp.setText(draft.getTimestamp());
        Bitmap bmp = BitmapFactory.decodeByteArray(draft.getImage(), 0, draft.getImage().length);
        holder.ImageView.setImageBitmap(bmp);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(DisplayImages.makeIntent(context, draft));
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert=new AlertDialog.Builder(context);
                alert.setMessage("ARE YOU SURE YOU WANT TO DELETE");
                alert.setCancelable(true);

                alert.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                new DeleteDraft(DraftsRecyclerAdapter.this).execute(draft);
                                dialog.cancel();
                            }
                        });

                alert.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = alert.create();
                alert11.show();
                notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return DraftList.size();
    }

    public class DraftsViewHolder extends RecyclerView.ViewHolder {

        TextView TitleTextView;
        TextView DescriptionTextView;
        TextView TimeStamp;
        ImageView ImageView;
        CardView cardView;
        TextView AreaTextView;
        Button btnDelete;

        public DraftsViewHolder(@NonNull View itemView) {
            super(itemView);
            TitleTextView = itemView.findViewById(R.id.TitleTextView);
            DescriptionTextView = itemView.findViewById(R.id.DescriptionTextView);
            TimeStamp = itemView.findViewById(R.id.TimeTextView);
            ImageView = itemView.findViewById(R.id.DraftImageView);
            AreaTextView = itemView.findViewById(R.id.AreaTextView);
            cardView = itemView.findViewById(R.id.DraftsCardView);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    private class DeleteDraft extends AsyncTask<Draft, Void, Void> {
        private DraftsRecyclerAdapter context;


        @Override
        protected Void doInBackground(Draft... drafts) {
            MySingleton.getInstance().getAppDatabase().draftDAO().delete(drafts[0]);
            Log.d(TAG, "doInBackground: " + "inserted");
//            Toast.makeText(context,"INSERTED TO DATABASE",Toast.LENGTH_LONG).show();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            DraftsRecyclerAdapter.this.notifyDataSetChanged();
        }

        public DeleteDraft(DraftsRecyclerAdapter context) {
            this.context = context;
        }
    }

}
