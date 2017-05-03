package com.example.datensammler17;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.datensammler17.Gyro.Gyro_Menu;
import com.example.datensammler17.Light.Light_Menu;


/**
 * Created by hubi on 01.11.16.
 */

public class MenuList extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listmenu);
    }

    public void gyro(View view){
        Intent i = new Intent(this,Gyro_Menu.class);
        startActivity(i);
    }

    public void light(View view){
        Intent i = new Intent(this,Light_Menu.class);
        startActivity(i);
    }
}
