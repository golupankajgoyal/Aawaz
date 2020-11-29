package com.example.aawaz.Message;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aawaz.R;


public class ReceiveTypeViewHolder extends RecyclerView.ViewHolder {

    public TextView receivedMessageTv;

    public ReceiveTypeViewHolder(@NonNull View itemView) {
        super(itemView);

        this.receivedMessageTv=itemView.findViewById(R.id.received_message_tv);
    }
}
