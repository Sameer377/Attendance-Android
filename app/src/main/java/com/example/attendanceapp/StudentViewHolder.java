package com.example.attendanceapp;

import android.view.ContextMenu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

public interface StudentViewHolder {
    void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo);

    @NonNull
    StudentAdapter.StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    void onBindViewHolder(@NonNull StudentAdapter.StudentViewHolder holder, int position);

    int getItemCount();
}
