package com.example.datensammler17.Gyro;

/**
 * Created by hubi on 01.11.16.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.datensammler17.R;

import java.util.ArrayList;

public class GyroArrayListAdapter extends ArrayAdapter<Gyro> {

    // declaring our ArrayList of items
    private ArrayList<Gyro> data;

    /* here we must override the constructor for ArrayAdapter
    * the only variable we care about now is ArrayList<Item> objects,
    * because it is the list of objects we want to display.
    */
    public GyroArrayListAdapter(Context context, int textViewResourceId, ArrayList<Gyro> objects) {
        super(context, textViewResourceId, objects);
        this.data = objects;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent){

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.listviewgyro, null);
        }

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 *
		 * Therefore, i refers to the current Item object.
		 */
        Gyro i = data.get(position);

        if (i != null) {

                // This is how you obtain a reference to the TextViews.
                // These TextViews are created in the XML files we defined.
                TextView idGyro = (TextView) v.findViewById(R.id.idGyro);
                TextView x = (TextView) v.findViewById(R.id.x);
                TextView y = (TextView) v.findViewById(R.id.y);
                TextView z = (TextView) v.findViewById(R.id.z);

                // check to see if each individual textview is null.
                // if not, assign some text!
                if (idGyro != null)
                    idGyro.setText("id: " + i.getId() + "   ");
                if (x != null)
                    x.setText("x: " + i.getX() + "   ");
                if (y != null)
                    y.setText("y: " + i.getY() + "   ");
                if (z != null)
                    z.setText("z: " + i.getZ() + "   ");

        }

        // the view must be returned to our activity
        return v;

    }

}