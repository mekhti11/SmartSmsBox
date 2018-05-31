package com.mekhti.smartsmsbox.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mekhti.smartsmsbox.Entity.Contact;
import com.mekhti.smartsmsbox.Entity.ContactType;
import com.mekhti.smartsmsbox.Entity.Sms;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class Sqlite_utils {

    private Context c;
    private DbHelper mDB;
    private SQLiteDatabase db;
    private SQLiteDatabase read;


    public Sqlite_utils(Context context) {
        c = context;
        mDB = new DbHelper(c);
    }

    public void open(){
        db = mDB.getWritableDatabase();
        read = mDB.getReadableDatabase();
    }

    public void close(){
        mDB.close();
    }

    public void addContacts(ArrayList<Contact> list){

        Cursor c  = db.rawQuery("select count(*) from contact_list ",null);
        c.moveToFirst();
        Log.d(TAG, "addContacts: "+c.getInt(0));
        if(c.getInt(0)>0){
            return;
        }

        ContentValues val = new ContentValues();
        for (Contact a : list) {
            val.put("name",a.getName());
            val.put("phone",a.getPhoneNum());
            val.put("contact_type",a.getType().toString());
            Log.d(TAG, "addContacts: "+val);
            db.insert("contact_list",null,val);

        }
    }

    public void addSMSs(ArrayList<Sms> list){

        Cursor c  = db.rawQuery("select count(*) from sms ",null);
        c.moveToFirst();
        if(c.getInt(0)>0){
            return;
        }

        Log.d(TAG, "addSMSs: "+list);
        ContentValues val = new ContentValues();
        for (Sms a : list) {
            val.put("senderNum",a.getSender());
            val.put("message",a.getMessage());
            //val.put("sms_type",a.getType().toString());
            Log.d(TAG, "addSMSs: "+val);
            db.insert("sms",null,val);

        }
    }


    public ArrayList<Sms> getSmsList(){
        ArrayList<String> s = new ArrayList<>();
        ArrayList<Sms> list = new ArrayList<>();
        Cursor c = db.rawQuery("select senderNum , message from sms ",null);
        c.moveToFirst();

        while (!c.isAfterLast()){

                s.add(c.getString(0));
                list.add(new Sms(c.getString(0), c.getString(1)));


            c.moveToNext();
        }
        return list;
    }

    public ArrayList<Contact> getWhiteList(){
        ArrayList<String> s = new ArrayList<>();
        String[] whereArgs = new String[]{"WhiteList"};
        ArrayList<Contact> list = new ArrayList<>();
        Cursor c = db.rawQuery("select name , phone from contact_list where contact_type = ?",whereArgs);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(!s.contains(c.getString(0))) {
                s.add(c.getString(0));
                list.add(new Contact(c.getString(0), c.getString(1), ContactType.WhiteList));

            }
            c.moveToNext();
        }
        return list;
    }

    public ArrayList<Contact> getBlackList(){
        ArrayList<String> s = new ArrayList<>();
        String[] whereArgs = new String[]{"BlackList"};
        ArrayList<Contact> list = new ArrayList<>();
        Cursor c = db.rawQuery("select name , phone from contact_list where contact_type = ?",whereArgs);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(!s.contains(c.getString(0))) {
                s.add(c.getString(0));
                list.add(new Contact(c.getString(0), c.getString(1), ContactType.BlackList));

            }
            c.moveToNext();
        }
        return list;
    }


    public void updateContacts(String name , String phone, ContactType type) {
        String[] args = {name };
        db.execSQL("update contact_list set contact_type = '"+type.toString()+"' where name = ? ",args);
    }

    public void addSMSs(String sender, String message) {
        ContentValues val = new ContentValues();

            val.put("senderNum",sender);
            val.put("message",message);
            //val.put("sms_type",a.getType().toString());
            db.insert("sms",null,val);
    }
}
