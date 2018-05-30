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

import com.mekhti.smartsmsbox.Entity.Contact;
import com.mekhti.smartsmsbox.Entity.ContactType;
import com.mekhti.smartsmsbox.utils.ContactUtils;

import java.util.ArrayList;

public class WhiteList extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mlistView;
    private ArrayList<Contact> contacts;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.white_list);

        setUI();

        setData();
    }

    private void setData() {
        mlistView = findViewById(R.id.white_list);

        ContactUtils cu = new ContactUtils(getApplicationContext());
        contacts = cu.getContacts();

        final ArrayList<String> Arr = new ArrayList<>();
        for(Contact c : contacts)
            if(c.getType() == ContactType.WhiteList)
                Arr.add(c.getName());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, R.id.text1, Arr);
                mlistView.setAdapter(adapter);
            }
        });

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "item clicked : \n" + contacts.get(position).getPhoneNum(), Toast.LENGTH_SHORT).show();
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
