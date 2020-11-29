package com.example.aawaz.Message;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.aawaz.MessageData;

import java.util.ArrayList;

public class MyDiffUtilCallBack extends DiffUtil.Callback {

    private ArrayList<MessageData> oldMessages;
    private ArrayList<MessageData> newMessages;

    public MyDiffUtilCallBack(ArrayList<MessageData> oldData, ArrayList<MessageData> newData){

        this.oldMessages=oldData;
        this.newMessages=newData;
    }

    @Override
    public int getOldListSize() {
        return oldMessages.size();
    }

    @Override
    public int getNewListSize() {
        return newMessages.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldMessages.get(oldItemPosition).getTime()==newMessages.get(newItemPosition).getTime();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldMessages.get(oldItemPosition).equals(newMessages.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
