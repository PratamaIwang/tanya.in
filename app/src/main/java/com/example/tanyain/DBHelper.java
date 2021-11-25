package com.example.tanyain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    public static final String database_name = "login.db";
    public static final String table_name = "table_login";

    public static final String row_id = "_id";
    public static final String row_username = "Username";
    public static final String row_password = "Password";

    private SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, database_name, null, 1);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query ="CREATE TABLE " + table_name + " (" + row_id +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + row_username + " TEXT," + row_password + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ table_name);
    }

    //Insert
    public void inserData(ContentValues values){
        db.insert(table_name,null,values);
    }

    public boolean checkUser(String username, String Password){
        String[] columns = {row_id};
        SQLiteDatabase db = getReadableDatabase();
        String selection = row_username + "=?" + " and " + row_password + "=?";
        String[] selectionArgs = {username,Password};
        Cursor cursor = db.query(table_name, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        if (count>0)
            return true;
        else
            return false;
    }
}
