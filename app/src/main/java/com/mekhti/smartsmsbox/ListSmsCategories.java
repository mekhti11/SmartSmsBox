package com.mekhti.smartsmsbox;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mekhti.smartsmsbox.Entity.SmsTypes;
import com.mekhti.smartsmsbox.utils.SmsUtils;

import java.util.ArrayList;

public class ListSmsCategories extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private final String[] categories= {SmsTypes.PERSONAL.toString(),SmsTypes.COMMERCIAL.toString(),
                                        SmsTypes.OTP.toString(),SmsTypes.SPAM.toString()};
    private ListView mListView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_sms_categories);

        setUI();

        setList();
    }

    private void setList() {
        mListView = findViewById(R.id.category_list);
        adapter = new ArrayAdapter<>
                (this, android.R.layout.simple_list_item_1, categories);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                try {
                    Intent i = new Intent(getApplicationContext(),ListSMSs.class);
                    i.putExtra("category",adapter.getItem(pos));
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setUI() {
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
