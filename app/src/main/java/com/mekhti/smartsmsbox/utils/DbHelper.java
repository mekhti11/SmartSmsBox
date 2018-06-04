package com.mekhti.smartsmsbox.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, "SmsBox", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table contact_list (id integer primary key autoincrement ," +
                " name text ," +
                " phone text ," +
                " contact_type text )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("drop table if exists sms");
    }
}
