package com.example.fa_aastha_c0851622_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    RecyclerView rv;
    DBHelper DB;
    ViewAdapter viewAdapter;




    ArrayList<String> id, name, lati, lng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        rv = findViewById(R.id.recycler_view_list);
        DB= new DBHelper(this);
        id = new ArrayList<>();
        name = new ArrayList<>();
        lati = new ArrayList<>();
        lng = new ArrayList<>();
        viewAdapter = new ViewAdapter(ListActivity.this, ListActivity.this, id, name, lati, lng);
        rv.setAdapter(viewAdapter);
        rv.setLayoutManager(new LinearLayoutManager(ListActivity.this));



        Cursor cursor = DB.getdata();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Products Found", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                id.add(cursor.getString(0));
                name.add(cursor.getString(1));
                lati.add(cursor.getString(2));
                lng.add(cursor.getString(3));


            }
        }
    }
}