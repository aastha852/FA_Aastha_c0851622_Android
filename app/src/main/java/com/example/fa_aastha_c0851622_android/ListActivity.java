package com.example.fa_aastha_c0851622_android;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
        name = new ArrayList<>();
        lati = new ArrayList<>();
        lng = new ArrayList<>();
        viewAdapter = new ViewAdapter(ListActivity.this, ListActivity.this, id, name, lati, lng);
        rv.setAdapter(viewAdapter);
        rv.setLayoutManager(new LinearLayoutManager(ListActivity.this));



        Cursor cur = DB.getdata();
        StringBuffer buffer = new StringBuffer();
        while(cur.moveToNext()){buffer.append("Name :"+cur.getString(0)+"\n");
            buffer.append("Lati :"+cur.getString(1)+"\n");
            buffer.append("Longi :"+cur.getString(2)+"\n\n");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
        builder.setCancelable(true);
        builder.setTitle("User Entries");
        builder.setMessage(buffer.toString());
        builder.show();

        if (cur.getCount() == 0) {
            Toast.makeText(this, "No Products Found", Toast.LENGTH_SHORT).show();
        } else {
            while (cur.moveToNext()) {
                name.add(cur.getString(0));
                lati.add(cur.getString(1));
                lng.add(cur.getString(2));
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }
}