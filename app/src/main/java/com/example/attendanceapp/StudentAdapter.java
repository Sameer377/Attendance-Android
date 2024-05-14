package com.example.attendanceapp;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private static ArrayList<StudentItem> studentItems;
    private static Context context;
    private static OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item,parent,false);
        return  new StudentAdapter.StudentViewHolder(v,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public StudentAdapter(Context context, ArrayList<StudentItem> studentItems) {
        this.studentItems = studentItems;
        this.context = context;
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder implements com.example.attendanceapp.StudentViewHolder {
        TextView roll;
        TextView name;
        TextView status;
        CardView cardView;



        public StudentViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            roll = itemView.findViewById(R.id.roll1);
            name = itemView.findViewById(R.id.name1);
            status = itemView.findViewById(R.id.status1);
            cardView = itemView.findViewById(R.id.cardview);

            itemView.setOnClickListener(v -> onItemClickListener.onClick(getAdapterPosition()));
            itemView.setOnCreateContextMenuListener((View.OnCreateContextMenuListener) this);



        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
            menu.add(getAdapterPosition(),0,0,"Edit");
            menu.add(getAdapterPosition(),1,0,"Delete");
        }

        @NonNull
        @Override
        public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false);
            return new StudentViewHolder(itemView, onItemClickListener);
        }


        @Override
        public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
            holder.roll.setText(studentItems.get(position).getRoll()+"");
            holder.name.setText(studentItems.get(position).getName());
            holder.status.setText(studentItems.get(position).getStatus());
            holder.cardView.setCardBackgroundColor(getColor(position));







        }

        private int getColor(int position) {
            String status=studentItems.get(position).getStatus();
            if (status.equals("P"))
                return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.present)));
            else if(status.equals("A"))
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.absent)));

            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.normal)));

        }


        @Override
        public int getItemCount() {
            return studentItems.size();
        }
    }
}
