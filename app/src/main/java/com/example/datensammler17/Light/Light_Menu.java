package com.example.datensammler17.Light;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.datensammler17.DBHandler;
import com.example.datensammler17.R;


/**
 * Created by J.Kost on 14.11.2016.
 */

public class Light_Menu extends AppCompatActivity {
    DBHandler db = new DBHandler(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.light_menu);

    }

    public void open(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("alert dialog");
        alertDialogBuilder.setMessage("conform to delete");
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        db.onUpgradeLight(db.getWritableDatabase(), 1, 1);
                    }
                });

        alertDialogBuilder.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void light(View view) {
        Intent i = new Intent(this, LightList.class);
        startActivity(i);
    }

    public void deleteAll(View view) {
        open(view);

    }
}
