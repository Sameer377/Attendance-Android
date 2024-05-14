package com.example.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Calendar;

public class SheetActivity extends AppCompatActivity {


    private String subjectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet);

        Intent intent = getIntent();
        subjectName=getIntent().getExtras().get("subject").toString();
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        title.setText(intent.getExtras().get("month").toString());
        TextView subTitle = toolbar.findViewById(R.id.subtitle_toolbar);
        subTitle.setText(subjectName);
        showTable();
    }

    @SuppressLint("ResourceAsColor")
    private void showTable() {

        DbHelper dbHelper=new DbHelper(this);
        TableLayout tableLayout=findViewById(R.id.tableLayout);
        long[] idArray=getIntent().getLongArrayExtra("idArray");
        long[] rollArray=getIntent().getLongArrayExtra("rollArray");
        String[] nameArray=getIntent().getStringArrayExtra("nameArray");
        String month=getIntent().getStringExtra("month");

        int DAY_IN_MONTH=getDayInMonth(month);

        //row setup


        int rowsize=idArray.length+1;
        TableRow[] rows=new TableRow[rowsize];
        TextView[] roll_tvs=new TextView[rowsize];
        TextView[] name_tvs=new TextView[rowsize];
        TextView[][] status_tvs=new TextView[rowsize][DAY_IN_MONTH+1];

        for (int i=0;i<rowsize;i++){
            roll_tvs[i]=new TextView(this);
            name_tvs[i]=new TextView(this);
            for (int j=0;j<DAY_IN_MONTH;j++){
                status_tvs[i][j]= new TextView(this);
            }
        }

        //header
        roll_tvs[0].setText("Roll");
        roll_tvs[0].setTypeface(roll_tvs[0].getTypeface(), Typeface.BOLD);


        name_tvs[0].setText("Name");
        name_tvs[0].setTypeface(name_tvs[0].getTypeface(), Typeface.BOLD);

        for (int i=1;i<DAY_IN_MONTH;i++){
            status_tvs[0][i].setText(String.valueOf(i));
            status_tvs[0][i].setTypeface(status_tvs[0][i].getTypeface(), Typeface.BOLD);
        }

        for (int i=0;i<idArray.length;i++){
            roll_tvs[i+1].setText(String.valueOf(rollArray[i]));
            name_tvs[i+1].setText(nameArray[i]);


            for (int j=1;j<DAY_IN_MONTH;j++){
                String day=String.valueOf(j);
                if (day.length()==1) day="0"+day;
                String date =day+"."+month;
                String status =dbHelper.getStatus(idArray[i],date);
                status_tvs[i+1][j].setText(status);


            }

        }
        for (int i=0;i<rowsize;i++){
            rows[i]=new TableRow(this);


            if (i%2==0)
                rows[i].setBackgroundColor(Color.parseColor("#EEEEEE"));
            else
                rows[i].setBackgroundColor(Color.parseColor("#E4E4E4"));

            if(i==0){
                rows[i].setBackgroundResource(R.color.header);
            }

            roll_tvs[i].setPadding(16,16,16,16);
            name_tvs[i].setPadding(16,16,16,16);

            rows[i].addView(roll_tvs[i]);
            rows[i].addView(name_tvs[i]);

            for (int j=1;j<DAY_IN_MONTH;j++){
                status_tvs[i][j].setPadding(16,16,16,16);
                if(status_tvs[i][j].getText().toString().trim()=="A"){
                    status_tvs[i+1][j].setTextColor(R.color.absent);
                }
                rows[i].addView(status_tvs[i][j]);
            }


            tableLayout.addView(rows[i]);
        }

        tableLayout.setShowDividers(TableLayout.SHOW_DIVIDER_MIDDLE);






    }

    private int getDayInMonth(String month) {
        int monthIndex=Integer.valueOf(month.substring(0,1));
        int year= Integer.valueOf(month.substring(4));

        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.MONTH,monthIndex);
        calendar.set(Calendar.YEAR,year);

        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}