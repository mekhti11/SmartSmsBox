package com.mekhti.smartsmsbox.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mekhti.smartsmsbox.Entity.Contact;
import com.mekhti.smartsmsbox.Entity.ContactType;
import com.mekhti.smartsmsbox.Entity.Sms;
import com.mekhti.smartsmsbox.Entity.SmsTypes;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class Sqlite_utils {

    private Context c;
    private DbHelper mDB;
    private SQLiteDatabase db;
    private SQLiteDatabase read;
    private String[] comm = new String[]{"DOMINOS","LUKOIL","ENPARA","9230","9333","IS BANKASI",
            "DSMART","9090","Sinemia","Cinemaximum","ENPARA     ","Enpara.com ","FINANSBANK ",
            "BEE PIZZA","LUKOIL"};


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

        db.execSQL("drop table if exists sms");
        String sql = "create table sms (id integer primary key autoincrement ," +
                " senderNum varchar(16) not null," +
                " message text ," +
                " sms_type text )";
        db.execSQL(sql);

//        Cursor c  = db.rawQuery("select count(*) from sms ",null);
//        c.moveToFirst();
//        Log.d(TAG, "addContacts: "+c.getInt(0));
//        if(c.getInt(0)>0){
//            return;
//        }
        Log.d(TAG, "addSMSs: "+list);
        ContentValues val = new ContentValues();
        for (Sms a : list) {
            val.put("senderNum",a.getSender());
            val.put("message",a.getMessage());
            val.put("sms_type",getSmsType(a.getSender(),a.getMessage()));
            Log.d(TAG, "addSMSs: "+val);
            db.insert("sms",null,val);

        }
    }


    public ArrayList<Sms> getSmsList(String type){
        open();
        ArrayList<String> s = new ArrayList<>();
        ArrayList<Sms> list = new ArrayList<>();
        String[] whereArgs = new String[] {type};
        Cursor c = read.rawQuery("select senderNum , message from sms where sms_type = ?",whereArgs);
        c.moveToFirst();
        Log.i(TAG, "getSmsList: ");
        while (!c.isAfterLast()){

                s.add(c.getString(0));
                list.add(new Sms(c.getString(0), c.getString(1)));


            c.moveToNext();
        }
        close();
        return list;
    }

    public ArrayList<Contact> getWhiteList(){
        ArrayList<String> s = new ArrayList<>();
        String[] whereArgs = new String[]{"WhiteList"};
        ArrayList<Contact> list = new ArrayList<>();
        Cursor c = read.rawQuery("select name , phone from contact_list where contact_type = ?",whereArgs);
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
        Cursor c = read.rawQuery("select name , phone from contact_list where contact_type = ?",whereArgs);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(!s.contains(c.getString(0)) && c.getString(1)!=null) {
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
        val.put("sms_type",getSmsType(sender,message));
        Log.d("new sms", "addSMSs: "+val);
        db.insert("sms",null,val);
    }

    public String getSmsType(String phoneNum,String message){
        String[] whereArgs = new String[]{phoneNum};

        for(String str : comm){
            if(str.equals(phoneNum)){
                return SmsTypes.COMMERCIAL.toString();
            }
        }

        Cursor c = read.rawQuery("select contact_type from contact_list where phone = ?",whereArgs);
        Log.d(TAG, "getSmsType: "+c);
        if (c!=null){
            c.moveToFirst();
            while (!c.isAfterLast()){
                if(c.getString(0).equals(ContactType.WhiteList.toString())) {
                    Log.d("Whitelist", "getContactType: "+phoneNum);
                    return SmsTypes.PERSONAL.toString();
                }else if(c.getString(0).equals(ContactType.BlackList.toString())) {
                    Log.d("BlackList", "getContactType: "+phoneNum);
                    return SmsTypes.SPAM.toString();
                }
                c.moveToNext();
            }
        }

        if(phoneNum.equals("Verify"))
            return SmsTypes.OTP.toString();

        return SmsTypes.SPAM.toString();
    }
}
