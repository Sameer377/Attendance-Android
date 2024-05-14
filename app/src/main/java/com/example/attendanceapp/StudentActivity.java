package com.example.attendanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {

    Toolbar toolbar;
    private String className;
    private String subjectName;
    private int position;
    private RecyclerView recyclerView;
    public StudentAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<StudentItem> studentItems = new ArrayList<>();

    private DbHelper dbHelper;
    private long cid;
    private MyCalender calender;
    private TextView  subtitle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        dbHelper=new DbHelper(this);

        Intent intent=getIntent();
        className= intent.getStringExtra("className");
        subjectName=intent.getStringExtra("subjectName");
        position=intent.getIntExtra("position",-1);
        cid=intent.getLongExtra("cid",-1);

        setToolbar();

      loadData();
       loadStatusData();
        recyclerView=findViewById(R.id.student_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set layout manager first
        adapter= new StudentAdapter(this, studentItems);
        adapter.setOnItemClickListener(position -> changeStatus(position));
        recyclerView.setAdapter(adapter);

    }

    private void loadData() {
        Cursor cursor=dbHelper.getStudentTable(cid);
        studentItems.clear();
        Toast.makeText(this, ""+cursor.getColumnIndex(DbHelper.S_ID)+" "+cursor.getColumnIndex(DbHelper.STUDENT_ROLL_KEY)+" "+cursor.getColumnIndex(DbHelper.STUDENT_NAME_KEY), Toast.LENGTH_SHORT).show();
        while (cursor.moveToNext()){
            @SuppressLint("Range") long sid= cursor.getLong(cursor.getColumnIndex(DbHelper.S_ID));
            @SuppressLint("Range") int roll = cursor.getInt(cursor.getColumnIndex(DbHelper.STUDENT_ROLL_KEY));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DbHelper.STUDENT_NAME_KEY));
            studentItems.add(new StudentItem(sid,roll,name));
        }
        cursor.close();

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            // Handle the case where studentAdapter is null
            Toast.makeText(this, "studentAdapter is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void changeStatus(int position) {
        String status=studentItems.get(position).getStatus();

        if(status.equals("P")) status="A";
        else  status="P";

        studentItems.get(position).setStatus(status);
        adapter.notifyItemChanged(position);
    }

    private void setToolbar(){


        toolbar = findViewById(R.id.toolbar);
        TextView title=toolbar.findViewById(R.id.title_toolbar);
        subtitle=toolbar.findViewById(R.id.subtitle_toolbar);
        calender=new MyCalender();
        ImageButton back=toolbar.findViewById(R.id.back);
        ImageButton save=toolbar.findViewById(R.id.save);
        save.setOnClickListener(v -> saveStatus());


        title.setText(className);
        if(calender!=null){
            subtitle.setText(subjectName+" | "+calender.getDate());

        }


        back.setOnClickListener(v -> onBackPressed());
        toolbar.inflateMenu(R.menu.student_menu);
        toolbar.setOnMenuItemClickListener(menuItem->onMenuItemClick(menuItem));
    }

    private void saveStatus() {
        for (StudentItem studentItem:studentItems){
            String status=studentItem.getStatus();
            if (status != "P") status="A";
            long value=dbHelper.addStatus(studentItem.getSid(),cid,calender.getDate(),status);

            if (value==-1)dbHelper.updateStatus(studentItem.getSid(),calender.getDate(),status);
        }
    }

    private void loadStatusData() {

        for (StudentItem studentItem:studentItems){
            String status=dbHelper.getStatus(studentItem.getSid(),calender.getDate());
            if (status!=null){
                studentItem.setStatus(status);
            }
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged(); // NullPointerException occurs here
        } else {
            // Handle the case where adapter is null
            Toast.makeText(this, "null adapter", Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(getApplicationContext(), ""+studentItems.size(), Toast.LENGTH_SHORT).show();

    }

    private boolean onMenuItemClick(MenuItem menuItem) {
        if(menuItem.getItemId()==R.id.add_student){
            showAddStudentDialog();
        }
        else if (menuItem.getItemId()==R.id.show_Calendar){
            showCalendar();
        }else if (menuItem.getItemId()==R.id.show_attendance_sheet){
            openSheetList();
        }
        return true;
    }

    private void openSheetList() {
        long[] idArray=new long[studentItems.size()];
        String[] nameArray=new String[studentItems.size()];
        int[] rollArray=new int[studentItems.size()];

        for (int i=0;i<idArray.length;i++)
            idArray[i]=studentItems.get(i).getSid();

        for (int i=0;i<rollArray.length;i++)
            rollArray[i]=studentItems.get(i).getRoll();

        for (int i=0;i<nameArray.length;i++)
            nameArray[i]=studentItems.get(i).getName();

        Intent intent=new Intent(this,SheetListActivity.class);
        intent.putExtra("cid",cid);
        intent.putExtra("idArray",idArray);
        intent.putExtra("rollArray",rollArray);
        intent.putExtra("nameArray",nameArray);


        startActivity(intent);
    }

    private void showCalendar() {

        calender.show(getSupportFragmentManager(),"");
        calender.setOnCalendarOkClickListener(this::onCalendarOkClicked);
    }

    private void onCalendarOkClicked(int year, int month, int day) {
        calender.setDate(year,month,day);
        subtitle.setText(subjectName+"|"+calender.getDate());
        loadStatusData();

    }

    private void showAddStudentDialog() {
        MyDialog dialog=new MyDialog();
        dialog.show(getSupportFragmentManager(),MyDialog.STUDENT_ADD_DIALOG);
        dialog.setListener((roll,name)->addStudent(roll,name));
    }

    private void addStudent(String roll_string, String name) {
        int roll=Integer.parseInt(roll_string);
        long sid= dbHelper.addStudent(cid,roll,name);
        StudentItem studentItem =new StudentItem(sid,roll,name);
        studentItems.add(studentItem);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case  0:
                showUpdateStudentDialog();
                break;
            case 1:
                deleteStudent(item.getGroupId());
        }
        return  super.onContextItemSelected(item);
    }


    private void showUpdateStudentDialog() {
        MyDialog dialog=new MyDialog(studentItems.get(position).getRoll(),studentItems.get(position).getName());
        dialog.show(getSupportFragmentManager(),MyDialog.STUDENT_UPDATE_DIALOG);
        dialog.setListener((roll_string,name)->updateStudent(position,name));
    }

    private void updateStudent(int position,  String name) {
        dbHelper.updateStudent(studentItems.get(position).getSid(),name);
        studentItems.get(position).setName(name);
    }

    private void deleteStudent(int position) {
        dbHelper.deleteStudent(studentItems.get(position).getSid());
        studentItems.remove(position);
        adapter.notifyItemRemoved(position);
    }



    public int getPosition() {
        return position;
    }
}