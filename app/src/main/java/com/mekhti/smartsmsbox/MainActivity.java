package com.mekhti.smartsmsbox;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mekhti.smartsmsbox.Entity.Sms;
import com.mekhti.smartsmsbox.utils.SmsUtils;
import com.mekhti.smartsmsbox.utils.Sqlite_utils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String INBOX_URI = "content://sms/inbox";
    private static MainActivity activity;
    private ArrayList<String> smsList = new ArrayList<>();
    private ListView mListView;
    private ArrayAdapter<String> adapter;

    Sqlite_utils s = new Sqlite_utils(this);

    public static MainActivity instance() {
        return activity;
    }
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = findViewById(R.id.list);
        s.open();
        ArrayList<Sms> smsL= s.getSmsList("OTP");
        for(Sms s:smsL){
            smsList.add(s.getSender()+"\n"+s.getMessage());
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, smsList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(MyItemClickListener);

    }

    @Override
    public void onStart() {
        super.onStart();
        activity = this;
    }



    public void updateList(String sender , String message) {
        adapter.insert(sender+"\n"+message,0);
        adapter.notifyDataSetChanged();
    }

    private AdapterView.OnItemClickListener MyItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
            try {
                Toast.makeText(getApplicationContext(), adapter.getItem(pos), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
