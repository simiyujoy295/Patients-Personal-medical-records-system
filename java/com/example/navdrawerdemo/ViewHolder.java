package com.example.navdrawerdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ConcurrentModificationException;

public class ViewHolder extends RecyclerView.ViewHolder {


    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClicklistener.onItemlongClick(view,getAdapterPosition());
                return false;
            }
        });
    }

    public void setData(Context context,String name,String age,String gender){
        TextView textView = itemView.findViewById(R.id.textview_row);

        textView.setText("Medical Condition: " + name + "\n"  + "Current Medication: "  + age +"\n"+ "Allergies: " + gender);
    }

    private ViewHolder.Clicklistener mClicklistener;
    public interface Clicklistener{
        void onItemlongClick(View view , int position);
    }

    public void setOnClickListener(ViewHolder.Clicklistener clickListener){
        mClicklistener = clickListener;
    }

}