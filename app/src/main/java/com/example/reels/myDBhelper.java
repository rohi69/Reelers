package com.example.reels;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.reels.models.logIn_model;

import java.util.ArrayList;

public class myDBhelper extends SQLiteOpenHelper {
    public static  final String LOGINTABLE = "login";
    public static final String EMAIL = "email";
    public static  final String PASS = "password";


    public myDBhelper(@Nullable Context context) {
        super(context, LOGINTABLE , null , 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ LOGINTABLE + "("+EMAIL +" text primary key , "+PASS +" text )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists "+ LOGINTABLE);
    }


    public void addData(String Email, String Pass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EMAIL, Email);
        values.put(PASS, Pass);
        db.insert(LOGINTABLE, null, values);

    }


    public ArrayList<logIn_model> fetchData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+LOGINTABLE , null);
        ArrayList<logIn_model> arr = new ArrayList<>();
        while (cursor.moveToNext()){
            logIn_model model = new logIn_model(cursor.getString(0)  , cursor.getString(1));
            arr.add(model);
        }
        cursor.close();

        return arr;
    }


    public boolean emailExists(String Email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+LOGINTABLE +" where " + EMAIL + " =? " , new String[]{Email});
         boolean exists = cursor.getCount()>0;
         cursor.close();

         return exists;
    }



}