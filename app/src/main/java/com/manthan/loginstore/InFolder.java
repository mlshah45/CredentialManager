package com.manthan.loginstore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manthan on 1/29/2015.
 */
public class InFolder extends Activity {

    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        Intent intent = getIntent();
        String folderName = intent.getStringExtra(MainLogin.NameOfTheFolder);
        listView =(ListView)findViewById(R.id.MainListView);
        ListElements lee = new ListElements();
        lee.setFolder(true);
        lee.setName(folderName);
        List list = new ArrayList();
        list.add(lee);
        CustomAdapter adapter = new CustomAdapter(InFolder.this,R.layout.list_row,list);
        listView.invalidateViews();
        //listView.
        listView.setAdapter(adapter);
    }
}
