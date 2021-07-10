package com.example.elevator_music;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ChattingRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    MediaPlayer mediaPlayer;
    boolean isPaused = true;
    boolean isFirst = true;
    boolean isReleased = false;
    int music_position = 0;

    public ChattingRecyclerAdapter(ArrayList<ChatItem> chatItems, Context context) {
        this.chatItems = chatItems;
        this.context = context;
    }

    Context context;
    ArrayList<ChatItem> chatItems;

    class ViewHolder0 extends RecyclerView.ViewHolder {
        TextView sentBody;

        ViewHolder0(@NonNull View itemView) {
            super(itemView);
            sentBody = itemView.findViewById(R.id.sentBody);

        }
    }

    class ViewHolder1 extends RecyclerView.ViewHolder {
        TextView receivedBody;

        ViewHolder1(@NonNull View itemView) {
            super(itemView);
            receivedBody = itemView.findViewById(R.id.receiveBody);
        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder {
        TextView cMusicTitle, cMusicArtist;
        ImageView cMusicImg;
        ImageButton musicPlayBtn;

        ViewHolder2(@NonNull View itemView) {
            super(itemView);
            musicPlayBtn = itemView.findViewById(R.id.music_play_btn);
            cMusicTitle = itemView.findViewById(R.id.cMusicTitle);
            cMusicArtist = itemView.findViewById(R.id.cMusicArtist);
            cMusicImg = itemView.findViewById(R.id.cMusicImg);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
            return new ViewHolder0(v);
        } else if (viewType == 1) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
            return new ViewHolder1(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_music, parent, false);
            return new ViewHolder2(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 0) {
            ViewHolder0 viewHolder0 = (ViewHolder0) holder;
            viewHolder0.sentBody.setText(chatItems.get(position).text);
        } else if (holder.getItemViewType() == 1) {
            ViewHolder1 viewHolder1 = (ViewHolder1) holder;
            viewHolder1.receivedBody.setText(chatItems.get(position).text);
        } else {
            ViewHolder2 viewHolder2 = (ViewHolder2) holder;
            Glide.with(context).load(chatItems.get(position).cover_img)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Log.e("TAG", "Error loading image", e);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(viewHolder2.cMusicImg);
            viewHolder2.cMusicArtist.setText(chatItems.get(position).artist_name);
            viewHolder2.cMusicTitle.setText(chatItems.get(position).music_name);
            Log.e(TAG, "onBindViewHolder: " + chatItems.get(position).preview_url);

            viewHolder2.musicPlayBtn.setOnClickListener(v -> {
                if (isFirst) {
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                        isReleased = true;
                    }
                    try {
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setDataSource(chatItems.get(position).preview_url);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        isPaused = false;
                        isFirst = false;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (mediaPlayer != null && !isPaused && !isReleased) {
                    isPaused = true;
                    music_position = mediaPlayer.getCurrentPosition();
                    mediaPlayer.pause();

                } else if (mediaPlayer != null && isPaused) {
                    mediaPlayer.start();
                    mediaPlayer.seekTo(music_position);
                    isPaused = false;
                }
            });

        }
    }

    @Override
    public int getItemViewType(int position) {
        return chatItems.get(position).getPosition();
    }

    @Override
    public int getItemCount() {
        return chatItems.size();
    }


}
