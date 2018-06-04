package com.mekhti.smartsmsbox.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.Html;
import android.widget.ArrayAdapter;

import com.mekhti.smartsmsbox.Entity.Contact;
import com.mekhti.smartsmsbox.Entity.Sms;
import com.mekhti.smartsmsbox.ListSmsCategories;
import com.mekhti.smartsmsbox.R;

import java.util.ArrayList;

public class SmsUtils {

    private static final String INBOX_URI = "content://sms/inbox";

    public SmsUtils() {
    }

    public ArrayList<Sms> readSMS(Context c)  {
        ArrayList<Sms> list = new ArrayList<>();
        ContentResolver contentResolver = c.getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse(INBOX_URI), null, null, null, null);
        int senderIndex = smsInboxCursor.getColumnIndex("address");
        int messageIndex = smsInboxCursor.getColumnIndex("body");
        if (messageIndex < 0 || !smsInboxCursor.moveToFirst()) return null;
        do {
            String sender = smsInboxCursor.getString(senderIndex);
            String message = smsInboxCursor.getString(messageIndex);
            list.add(new Sms(sender,message));

        } while (smsInboxCursor.moveToNext());
        return list;
    }




}
