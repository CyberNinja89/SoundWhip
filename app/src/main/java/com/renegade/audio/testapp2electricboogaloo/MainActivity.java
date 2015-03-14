package com.renegade.audio.testapp2electricboogaloo;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    public static MainActivity instance;
    ListView ListViewsms;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> smsList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        ListViewsms = (ListView) findViewById(R.id.SMSList);
        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, smsList);
        ListViewsms.setAdapter(arrayAdapter);
        loadSMSlist();
    }

    private void loadSMSlist() {
        ContentResolver contentResolver = this.getContentResolver();
        Uri uri = Uri.parse("content://sms//inbox");
        Cursor smscursor = contentResolver.query(uri, null, null, null, null);
        int indexBody = smscursor.getColumnIndex("body");
        int indexAddress = smscursor.getColumnIndex("address");
        if (indexBody < 0 || !smscursor.moveToFirst()) return;
        arrayAdapter.clear();
        //do {
            String str = "SMS From: " + smscursor.getString(indexAddress) +
                    "\n" + smscursor.getString(indexBody  ) + "\n";
            //arrayAdapter.add(str);
       // } while (smscursor.moveToNext());
    }

    public void updatelist(String newMsg)
    {
        arrayAdapter.insert(newMsg, 0);
    }
}
