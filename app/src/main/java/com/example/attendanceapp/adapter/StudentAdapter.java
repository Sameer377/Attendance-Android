package com.example.attendanceapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendanceapp.R;
import com.example.attendanceapp.StudentItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter< StudentAdapter.StudentViewholder>{
    Context context;
    ArrayList<StudentItem> list;
    public StudentAdapter(Context context, ArrayList<StudentItem> list,OnItemClickListener listener) {
        this.context =context;
        this.list=list;
        this.listener=listener;
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener listener;
    @NotNull
    @Override
    public StudentViewholder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item,parent,false);
        return  new StudentViewholder(v);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NotNull StudentViewholder holder, int position)
    {

        StudentItem model = list.get(position);

        holder.name.setText(model.getName());
        holder.roll.setText(String.valueOf(model.getRoll()));
        holder.status.setText(String.valueOf(model.getStatus()));

        if(model.getStatus().trim()=="P"){
            holder.cardView.setBackgroundResource(R.color.present);
        }else if(model.getStatus().trim()=="A"){
            holder.cardView.setBackgroundResource(R.color.absent);
        }else{
            holder.cardView.setBackgroundResource(R.color.normal);
        }


    }




    @Override
    public int getItemCount() {
        return list.size();
    }

    // Sub Class to create references of the views in Crad
    // view (here "CommunityModel.xml")
    public  class StudentViewholder extends RecyclerView.ViewHolder {

      public  TextView roll;
        public TextView name;
        public  TextView status;
        public  CardView cardView;
        public StudentViewholder(@NotNull View itemView)
        {
            super(itemView);

            this.roll = itemView.findViewById(R.id.roll1);
            this.name = itemView.findViewById(R.id.name1);
            this.status = itemView.findViewById(R.id.status1);
            this.cardView = itemView.findViewById(R.id.cardview);

            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}