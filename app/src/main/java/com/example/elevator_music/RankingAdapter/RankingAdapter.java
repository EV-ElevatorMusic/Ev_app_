package com.example.elevator_music.RankingAdapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.elevator_music.R;
import com.example.elevator_music.Retrofit.Data;

import java.io.IOException;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.ItemViewHolder>{
    ArrayList<Data> arrayList;
    Context context;
    MediaPlayer mediaPlayer;
    static boolean isPaused = true;
    boolean isFirst = true;
    static boolean isReleased = false;
    int music_position = 0;

    public RankingAdapter(ArrayList<Data> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RankingAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ranking, parent, false);
        return new RankingAdapter.ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingAdapter.ItemViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(arrayList.get(position).getCover_img())
                .into(holder.ranking_Iv);

        holder.ranking_title.setText(arrayList.get(position).getMusic_name());
        holder.ranking_artist.setText(arrayList.get(position).getArtist_name());
        holder.ranking_btn.setOnClickListener(v -> {
            if (isFirst && isPaused) {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    isReleased = true;
                }
                try {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(arrayList.get(position).getPreview_url());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    holder.ranking_btn.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
                    isPaused = false;
                    isFirst = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (mediaPlayer != null && !isPaused && !isReleased) {
                isPaused = true;
                music_position = mediaPlayer.getCurrentPosition();
                mediaPlayer.pause();
                holder.ranking_btn.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);

            } else if (mediaPlayer != null && isPaused) {
                mediaPlayer.start();
                holder.ranking_btn.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
                mediaPlayer.seekTo(music_position);
                isPaused = false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView ranking_title, ranking_artist;
        ImageView ranking_Iv;
        ImageButton ranking_btn;
        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ranking_title = itemView.findViewById(R.id.ranking_title);
            ranking_Iv = itemView.findViewById(R.id.ranking_Iv);
            ranking_artist = itemView.findViewById(R.id.ranking_artist);
            ranking_btn = itemView.findViewById(R.id.ranking_btn);


        }
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
}
