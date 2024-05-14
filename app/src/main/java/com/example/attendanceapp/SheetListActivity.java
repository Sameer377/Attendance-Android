package com.example.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SheetListActivity extends AppCompatActivity {

    private ListView sheetList;
    private ArrayAdapter adapter;
    private ArrayList <String> listItems=new ArrayList();
    private long cid;
    long[] idArray,rollArray;
    String[] nameArray;
    String subjectName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_list);

        cid=getIntent().getLongExtra("cid",-1);
        Log.i("1234567890","onCreate: "+cid);
        subjectName=getIntent().getExtras().get("subject").toString();
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        title.setText(getIntent().getExtras().get("classname").toString());
        TextView subTitle = toolbar.findViewById(R.id.subtitle_toolbar);
        subTitle.setText(subjectName);
         idArray=getIntent().getLongArrayExtra("idArray");
         rollArray=getIntent().getLongArrayExtra("rollArray");
         nameArray=getIntent().getStringArrayExtra("nameArray");
        sheetList=findViewById(R.id.sheetList);
        adapter=new ArrayAdapter(this,R.layout.sheet_list, R.id.textView,listItems);
        sheetList.setAdapter(adapter);
        loadListItems();

        sheetList.setOnItemClickListener((parent, view, position, id) -> openSheetActivity(position));
    }

    private void openSheetActivity(int position) {


      /*  if(idArray!=null&&rollArray!=null*//*&&nameArray!=null*//*){

        }else{
            Toast.makeText(this, "null array", Toast.LENGTH_SHORT).show();
            return;
        }*/

        Intent intent=new Intent(this,SheetActivity.class);
        intent.putExtra("idArray",idArray);
        intent.putExtra("rollArray",rollArray);
        intent.putExtra("nameArray",nameArray);
        intent.putExtra("month",listItems.get(position));
        intent.putExtra("subject",subjectName);
        startActivity(intent);
    }

    private void loadListItems() {
        Cursor cursor=new DbHelper(this).getDistinctMonths(cid);

        while (cursor.moveToNext()){
            @SuppressLint("Range") String date=cursor.getString(cursor.getColumnIndex(DbHelper.DATE_KEY));//01.04.2020
            listItems.add(date.substring(3));
        }
        Toast.makeText(this, ""+listItems.get(0), Toast.LENGTH_SHORT).show();

        adapter.notifyDataSetChanged();
    }
}