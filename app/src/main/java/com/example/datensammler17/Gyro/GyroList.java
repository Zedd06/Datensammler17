package com.example.datensammler17.Gyro;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;


import com.example.datensammler17.DBHandler;
import com.example.datensammler17.R;

import java.util.ArrayList;

/**
 * Created by hubi on 01.11.16.
 */
public class GyroList extends AppCompatActivity {

    private ListView lv;
    DBHandler db = new DBHandler(this);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liste);

        lv = (ListView) findViewById(R.id.liste);

        // Instanciating an array list (you don't need to do this,
        // you already have yours)
        ArrayList<Gyro> data = (ArrayList)db.getAllGYRO();
        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        GyroArrayListAdapter adapter = new GyroArrayListAdapter(this,R.layout.listviewgyro,data);

        lv.setAdapter(adapter);
    }
}