package com.mekhti.smartsmsbox.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.mekhti.smartsmsbox.Entity.Contact;
import com.mekhti.smartsmsbox.Entity.ContactType;

import java.util.ArrayList;

public class ContactUtils {

    private ArrayList<Contact> contacts;
    private Context c;

    public ContactUtils(Context c){
        this.c = c;
        contacts = fromPhone();
    }


    //Todo > this method will get whitelist from Sqlite Db
    public ArrayList<Contact> getWhiteList(){
        ArrayList<Contact> whiteList = new ArrayList<>();

        return whiteList;
    }

    //Todo > this method will get blacklist from Sqlite Db
    public ArrayList<Contact> getBlackList(){
        ArrayList<Contact> whiteList = new ArrayList<>();

        return whiteList;
    }


    //Todo > this method will add contact list to Sqlite Db
    private void addDb(){


    }

    /**
     *The method below reads contact name and phone numbers from device
     */
    public ArrayList<Contact> fromPhone(){
        ArrayList<Contact> list = new ArrayList<>();
        Cursor cursor ;
        cursor = c.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,null, null, null);
        while (cursor.moveToNext()) {

            String name = cursor.getString(cursor.getColumnIndex
                    (ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            String phoneNumber = cursor.getString(cursor.getColumnIndex
                    (ContactsContract.CommonDataKinds.Phone.NUMBER));
            list.add(new Contact(name,phoneNumber, ContactType.WhiteList));
        }
        cursor.close();
        return list;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }
}