package com.example.elevator_music;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChattingRecyclerAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public ChattingRecyclerAdapter(ArrayList<ChatItem> chatItems) {
        this.chatItems = chatItems;
    }

    ArrayList<ChatItem> chatItems;
    class ViewHolder0 extends RecyclerView.ViewHolder{
        TextView sentBody;
        ViewHolder0(@NonNull View itemView) {
            super(itemView);
            sentBody=itemView.findViewById(R.id.sentBody);

        }
    }
    class ViewHolder1 extends RecyclerView.ViewHolder{
        TextView receivedBody;
        ViewHolder1(@NonNull View itemView) {
            super(itemView);
            receivedBody=itemView.findViewById(R.id.receiveBody);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==0){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
            return new ViewHolder0(v);
        }
        else{
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
            return new ViewHolder1(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType()==0){
            ViewHolder0 viewHolder0 = (ViewHolder0)holder;
            viewHolder0.sentBody.setText(chatItems.get(position).text);
        }
        else{
            ViewHolder1 viewHolder1 = (ViewHolder1)holder;
            viewHolder1.receivedBody.setText(chatItems.get(position).text);
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
