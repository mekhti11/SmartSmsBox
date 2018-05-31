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
import android.widget.Toast;

import com.mekhti.smartsmsbox.Entity.Contact;
import com.mekhti.smartsmsbox.Entity.ContactType;
import com.mekhti.smartsmsbox.utils.ContactUtils;
import com.mekhti.smartsmsbox.utils.Sqlite_utils;

import java.util.ArrayList;

public class WhiteList extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mlistView;
    private ArrayList<Contact> contacts;
    ContactUtils cu ;
    ArrayList<String> Arr ;
    ArrayAdapter<String> adapter;
    Sqlite_utils db = new Sqlite_utils(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.white_list);

        cu = new ContactUtils(getApplicationContext());
        Arr = new ArrayList<>();

        db.open();
        setUI();

        setData();
    }

    private void setData() {
        mlistView = findViewById(R.id.white_list);
        contacts = db.getWhiteList();
        for (Contact c : contacts){
            if (c.getType() == ContactType.WhiteList) {
                Arr.add(c.getName());
                Log.d("TAG", c.getName() + "\n" + c.getType().toString());
            }
        }
        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, R.id.text1, Arr);
        Log.d("RUN", "run: ");
        mlistView.setAdapter(adapter);
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                CharSequence colors[] = new CharSequence[] {
                        contacts.get(position).getPhoneNum(),
                        "Add to BlackList"};
                AlertDialog.Builder builder = new AlertDialog.Builder(WhiteList.this);
                builder.setTitle("");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 1){
                            Contact c = contacts.get(position);
                            db.updateContacts(c.getName(),c.getPhoneNum(),ContactType.BlackList);
                            String str = Arr.get(position);
                            contacts.remove(position);
                            Arr.remove(str);
                            adapter.remove(str);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                builder.show();
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
