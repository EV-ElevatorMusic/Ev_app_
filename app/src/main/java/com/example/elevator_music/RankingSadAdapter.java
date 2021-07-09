package com.example.elevator_music;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.elevator_music.Retrofit.Data;

import java.util.ArrayList;

public class RankingSadAdapter extends RecyclerView.Adapter<RankingSadAdapter.ItemViewHolder>{
    ArrayList<Data> arrayList;
    Context context;

    public RankingSadAdapter(ArrayList<Data> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RankingSadAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ranking, parent, false);
        return new RankingSadAdapter.ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingSadAdapter.ItemViewHolder holder, int position) {
//        String id = arrayList.get(position).substring(arrayList.get(position).lastIndexOf("/")+1);
//        String url ="https://img.youtube.com/vi/"+ id+ "/" + "default.jpg";  //유튜브 썸네일 불러오는 방법
//        Glide.with(context).load(url).asBitmap()
//                .format(DecodeFormat.PREFER_ARGB_8888)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(holder.lv_rangking);
        //Document doc = Jsoup.connect("").get();

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
//        ImageView lv_rangking;
//        TextView tv_title;
        TextView ranking_title, ranking_artist;
        ImageView ranking_Iv;
        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
//            lv_rangking = itemView.findViewById(R.id.ranking_Iv);
//            tv_title = itemView.findViewById(R.id.rankTitle);
            ranking_title = itemView.findViewById(R.id.ranking_title);
            ranking_Iv = itemView.findViewById(R.id.ranking_Iv);
            ranking_artist = itemView.findViewById(R.id.ranking_artist);
        }
    }
}
