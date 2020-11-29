package com.example.aawaz.Message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aawaz.MessageData;
import com.example.aawaz.R;

import java.util.ArrayList;

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<MessageData> messages;
    private Context mContext;

    public MessageRecyclerViewAdapter(ArrayList<MessageData> data, Context context){

        this.messages=data;
        this.mContext=context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {

            case MessageData.TYPE_RECEIVE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
                return new ReceiveTypeViewHolder(view);
            case MessageData.TYPE_SEND:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
                return new SendTypeViewHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessageData currentMessage = messages.get(position);

        if (currentMessage != null) {

            switch (currentMessage.getType()) {

                case MessageData.TYPE_RECEIVE:
                    ((ReceiveTypeViewHolder) holder).receivedMessageTv.setText(currentMessage.getMessage());
                    break;
                case MessageData.TYPE_SEND:
                    ((SendTypeViewHolder) holder).sendMessageTv.setText(currentMessage.getMessage());
                    break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {

        switch (messages.get(position).getType()) {
            case 1:
                return MessageData.TYPE_RECEIVE;
            case 2:
                return MessageData.TYPE_SEND;
            default:
                return -1;
        }
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void updateList (ArrayList<MessageData> newData) {

        DiffUtil.DiffResult diffResult= DiffUtil.calculateDiff(new MyDiffUtilCallBack(this.messages,newData));
        diffResult.dispatchUpdatesTo(this);
        messages.clear();
        this.messages.addAll(newData);
    }
}
