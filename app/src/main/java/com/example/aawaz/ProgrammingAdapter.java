package com.example.aawaz;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProgrammingAdapter extends RecyclerView.Adapter<ProgrammingAdapter.ProgrammingViewHolder> {

    private ArrayList<String> data;
    private Context context;

    public ProgrammingAdapter(ArrayList<String> data, Context context)
    {
        this.data = data;
        this.context=context;
    }
    @Override
    public ProgrammingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_layout, parent,false);

        return new ProgrammingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgrammingViewHolder holder, int position) {
        String title = data.get(position);
        holder.txtTitle.setText(title);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void getUpdated(ArrayList<String> newData){
        data=newData;
        notifyDataSetChanged();
    }

    public class ProgrammingViewHolder extends RecyclerView.ViewHolder{
        ImageView imgIcon;
        TextView txtTitle;
        public ProgrammingViewHolder(View itemView){
            super(itemView);

            imgIcon =itemView.findViewById(R.id.imgIcon);
            txtTitle =itemView.findViewById(R.id.txtTitle);
            Glide.with(context).load(R.drawable.user)
                    .circleCrop()
                    .centerCrop()
                    .into(imgIcon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    intent.putExtra("phoneNumber", txtTitle.getText());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
