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
    public static final String TABLE_NAME="favouriteloc";
    public static final String ID="id";
    public static final String NAME="name";
    public static final String LAT="lati";
    public static final String LONG="lng";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.c=context;
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        String sql =
                "CREATE TABLE IF NOT EXISTS favouriteloc("+
                        "id INTEGER CONSTRAINT pk PRIMARY KEY AUTOINCREMENT,"+
                        "name VARCHAR(20) NOT NULL,"+
                        "lati DOUBLE,"+
                        "long DOUBLE NOT NULL);";
        DB.execSQL(sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int ii) {
        DB.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
    }
    public void insertuserdata(String name ,double lati, double lng )
    {
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
        long result = db.update("favouriteloc",cv,"id=?",new String[]{id1});
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
        Cursor cursor = DB.rawQuery("Select * from Userdetails", null);
        return cursor;
    }
}