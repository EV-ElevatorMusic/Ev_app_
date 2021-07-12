package com.example.elevator_music.RankingAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.elevator_music.R;
import com.example.elevator_music.Retrofit.Data;

import java.util.ArrayList;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.ItemViewHolder>{
    ArrayList<Data> arrayList;
    Context context;

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

        holder.ranking_title.setText(arrayList.get(position).getMusic_name().toString());
        holder.ranking_artist.setText(arrayList.get(position).getArtist_name().toString());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView ranking_title, ranking_artist;
        ImageView ranking_Iv;
        Button ranking_btn;
        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ranking_title = itemView.findViewById(R.id.ranking_title);
            ranking_Iv = itemView.findViewById(R.id.ranking_Iv);
            ranking_artist = itemView.findViewById(R.id.ranking_artist);
            ranking_btn = itemView.findViewById(R.id.ranking_btn);

            ranking_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(mListener != null)
                            mListener.onItemClick(v, pos);
                    }
                }
            });
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
