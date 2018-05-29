package com.mekhti.smartsmsbox.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.Html;
import android.widget.ArrayAdapter;

import com.mekhti.smartsmsbox.ListSmsCategories;
import com.mekhti.smartsmsbox.R;

public class SmsUtils {

    private static final String INBOX_URI = "content://sms/inbox";
    private final String[] categories= {"Personal","Commercial","OTP","Spam"};

    public SmsUtils() {
    }

    public void readSMS(ArrayAdapter<String> adapter, Context c)  {
        ContentResolver contentResolver = c.getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse(INBOX_URI), null, null, null, null);
        int senderIndex = smsInboxCursor.getColumnIndex("address");
        int messageIndex = smsInboxCursor.getColumnIndex("body");
        if (messageIndex < 0 || !smsInboxCursor.moveToFirst()) return;
        adapter.clear();
        do {
            String sender = smsInboxCursor.getString(senderIndex);
            String message = smsInboxCursor.getString(messageIndex);
            String formattedText = String.format(c.getResources().getString(R.string.sms_message), sender, message);
            adapter.add(Html.fromHtml(formattedText).toString());
        } while (smsInboxCursor.moveToNext());
    }

    public void updateList(ArrayAdapter<String> adapter,final String newSms) {
        adapter.insert(newSms, 0);
        adapter.notifyDataSetChanged();
    }

    public void setCategories(ArrayAdapter<String> adapter) {
        for (String str: categories) {
            adapter.add(str);
        }

    }
}
