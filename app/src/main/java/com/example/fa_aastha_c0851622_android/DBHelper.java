package com.example.fa_aastha_c0851622_android;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {
    private Context c;
    public static final String DATABASE_NAME="Locationdb";
    public static final int DATABASE_VERSION=1;
    public static final String TABLE_NAME="LocationDB";
    public static final String ID="id";
    public static final String NAME="name";
    public static final String LAT="lati";
    public static final String LONG="lng";

    public DBHelper(Context context){
        super(context, "DatabaseLocat.db", null, 1);
        this.c=context;
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {

      DB.execSQL("create Table LocationDB(name TEXT primary key, lati double, lng double)");

    }


    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int ii) {
        DB.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
    }
    public void insertuserdata(String name ,double lati, double lng )
    {
        System.out.println("The values requested to be inserted are:");
        System.out.println("NAME:"+name);
        System.out.println("LATITUDE:"+lati);
        System.out.println("LONGITUDE:"+lng);

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(NAME,name);
        cv.put(LAT,lati);
        cv.put(LONG,lng);
        long result = db.insert(TABLE_NAME,null,cv);
        if (result == -1)
        {
            Toast.makeText(c, "Adding Failed", Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(c, "Location Added To Fav List", Toast.LENGTH_SHORT).show();
        }
    }
    public void updateuserdata(String id1,String name, double la, double lo)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(NAME,name);
        cv.put(LAT,la);
        cv.put(LONG,lo);
        long result = db.update("LocationDB",cv,"id=?",new String[]{id1});
        if (result == -1)
        {
            Toast.makeText(c, "Updating Failed", Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(c, "Updated Successfully", Toast.LENGTH_SHORT).show();
        }
    }
    public void deletedata (String deleteID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        long result =db.delete(TABLE_NAME,"id=?",new String[]{deleteID});
        if (result == -1)
        {
            Toast.makeText(c, "Deletion Failed", Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(c, "Deleted Successfully", Toast.LENGTH_SHORT).show();
        }

    }

    public Cursor getdata () {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from LocationDB", null);
        return cursor;
    }
}