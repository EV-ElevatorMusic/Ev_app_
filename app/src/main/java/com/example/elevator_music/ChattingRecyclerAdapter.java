package com.example.elevator_music;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static android.content.ContentValues.TAG;

public class ChattingRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    MediaPlayer mediaPlayer;
    static boolean isPaused = true;
    boolean isFirst = true;
    static boolean isReleased = false;
    int music_position = 0;
    Context context;
    ArrayList<ChatItem> chatItems;

    public ChattingRecyclerAdapter(ArrayList<ChatItem> chatItems, Context context) {
        this.chatItems = chatItems;
        this.context = context;
    }

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
        ImageButton musicPlayBtn, musicLikeBtn;

        ViewHolder2(@NonNull View itemView) {
            super(itemView);
            musicLikeBtn = itemView.findViewById(R.id.music_like_btn);
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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

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
                if (isFirst && isPaused) {
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                        isReleased = true;
                    }
                    try {
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setDataSource(chatItems.get(position).preview_url);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        viewHolder2.musicPlayBtn.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
                        isPaused = false;
                        isFirst = false;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (mediaPlayer != null && !isPaused && !isReleased) {
                    isPaused = true;
                    music_position = mediaPlayer.getCurrentPosition();
                    mediaPlayer.pause();
                    viewHolder2.musicPlayBtn.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);

                } else if (mediaPlayer != null && isPaused) {
                    mediaPlayer.start();
                    viewHolder2.musicPlayBtn.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
                    mediaPlayer.seekTo(music_position);
                    isPaused = false;
                }
            });
            AtomicBoolean isLiked = new AtomicBoolean(sharedPreferences.getBoolean(chatItems.get(position).music_name, false));
            if (!isLiked.get()) {
                viewHolder2.musicLikeBtn.setImageResource(R.drawable.ic_outline_thumb_up_24);
                Log.d(TAG, "onBindViewHolder: " + false);
            }
            else {
                viewHolder2.musicLikeBtn.setImageResource(R.drawable.ic_baseline_thumb_up_24);
                Log.d(TAG, "onBindViewHolder: " + true);
            }

            viewHolder2.musicLikeBtn.setOnClickListener(v -> {
                isLiked.set(sharedPreferences.getBoolean(chatItems.get(position).music_name, false));
                RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                StringRequest stringRequest;


                if (!isLiked.get()){
                    String url = "http://34.64.94.120/music/like?title="+chatItems.get(position).music_name;
                    Log.d(TAG, "onBindViewHolder: isLikedFalse");
                    stringRequest = new StringRequest(Request.Method.GET, url,
                            response -> {
                                viewHolder2.musicLikeBtn.setImageResource(R.drawable.ic_baseline_thumb_up_24);
                                editor.putBoolean(chatItems.get(position).music_name, true);
                                editor.apply();
                            }, error -> {
                        Toast.makeText(context, "좋아요 실패", Toast.LENGTH_SHORT).show();
                    });

                }else{
                    String url = "http://34.64.94.120/music/cancel_like?title="+chatItems.get(position).music_name;

                    stringRequest = new StringRequest(Request.Method.GET, url,
                            response -> {
                                viewHolder2.musicLikeBtn.setImageResource(R.drawable.ic_outline_thumb_up_24);
                                editor.putBoolean(chatItems.get(position).music_name, false);
                                editor.apply();
                            }, error -> Toast.makeText(context, "좋아요 취소 실패", Toast.LENGTH_SHORT).show());
                }

                queue.add(stringRequest);
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
