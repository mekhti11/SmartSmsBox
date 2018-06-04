package com.mekhti.smartsmsbox;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.mekhti.smartsmsbox.Entity.Sms;
import com.mekhti.smartsmsbox.utils.SmsUtils;
import com.mekhti.smartsmsbox.utils.Sqlite_utils;

import java.util.ArrayList;

public class ListSMSs extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private static ListSMSs activity;
    private TextView tv;
    private ListView listView;
    private static ArrayList<String> Arr ;
    private static ArrayAdapter<String> adapter;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_smss);



        setMenu();
        setData();

    }

    public void setData() {
        Arr = new ArrayList<>();
        Sqlite_utils db = new Sqlite_utils(this);
        listView = findViewById(R.id.sms_list);
        tv = findViewById(R.id.sms_type);
        //db.open();
        category = getIntent().getStringExtra("category");
        tv.setText(category);

        final ArrayList<Sms> smsL= db.getSmsList(category);
        Log.d("listSMSs", "onCreate: "+ smsL.toString());
        if(getIntent().hasExtra("sender")){
            String sender = getIntent().getStringExtra("sender");
            String message = getIntent().getStringExtra("message");
            smsL.add(0,new Sms(sender,message));
        }
        for(Sms s:smsL){
            Arr.add(s.getSender()+"\n"+s.getMessage());
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Arr);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                CharSequence colors[] = new CharSequence[] {
                        smsL.get(position).getSender(),
                        "Show Location",};
                AlertDialog.Builder builder = new AlertDialog.Builder(ListSMSs.this);
                builder.setTitle("");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 1){

                        }
                    }
                });
                builder.show();
            }
        });

    }

    public static ListSMSs instance() {
        return activity;
    }
    @Override
    public void onStart() {
        super.onStart();
        activity = this;
    }

    public void setMenu() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.icons8_xbox_menu_24);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()){
                            case R.id.home_page : {
                                Intent i = new Intent(getApplicationContext(),HomePage.class);
                                startActivity(i);
                                break;
                            }
                            case R.id.black_list : {
                                Intent i = new Intent(getApplicationContext(),BlackList.class);
                                startActivity(i);
                                break;
                            }
                            case R.id.white_list: {
                                Intent i = new Intent(getApplicationContext(),WhiteList.class);
                                startActivity(i);
                                break;
                            }
                            case R.id.sent_sms : {
                                Intent i = new Intent(getApplicationContext(),SendSms.class);
                                startActivity(i);
                                break;
                            }
                            case R.id.list_sms : {
                                Intent i = new Intent(getApplicationContext(),ListSmsCategories.class);
                                startActivity(i);
                                break;
                            }
                        }

                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
