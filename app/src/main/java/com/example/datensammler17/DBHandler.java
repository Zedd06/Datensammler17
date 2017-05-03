package com.example.datensammler17;

/**
 * Created by J.Kost on 08.11.2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.datensammler17.Gyro.Gyro;
import com.example.datensammler17.Light.Light;

import java.util.ArrayList;
import java.util.List;


public class DBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "sensor_Info";

    //--------------------Gyro Table
    // Contacts table name
    private static final String TABLE_GYRO = "gyro";
    // Gyro Table Columns names
    private static final String KEY_GYRO_ID = "id";
    private static final String KEY_X = "X";
    private static final String KEY_Y = "Y";
    private static final String KEY_Z = "Z";

    //--------------------Light Table
    // Contacts table name
    private static final String TABLE_LIGHT = "light";
    // Light Table Columns names
    private static final String KEY_LIGHT_ID = "id";
    private static final String KEY_LUX = "lux";


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_GYRO_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_GYRO +" ("
                + KEY_GYRO_ID + " INTEGER PRIMARY KEY," + KEY_X + " REAL,"
                + KEY_Y + " REAL," + KEY_Z + " REAL"+ " )";
        String CREATE_LIGHT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_LIGHT +" ("
                + KEY_LIGHT_ID + " INTEGER PRIMARY KEY," + KEY_LUX + " REAL"
                + " )";
        db.execSQL(CREATE_GYRO_TABLE);
        db.execSQL(CREATE_LIGHT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_GYRO);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_LIGHT);
        onCreate(db);
    }

    public void onUpgradeGyro(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GYRO);
// Creating tables again
        onCreate(db);
    }
    public void onUpgradeLight(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIGHT);
// Creating tables again
        onCreate(db);
    }



    //-------------------- DB_GYRO_Methods
    // Adding new gyro
    public void addGYRO(Gyro gyro) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_X, gyro.getX()); // X-Achse
        values.put(KEY_Y, gyro.getY()); // Y-Achse
        values.put(KEY_Z, gyro.getZ()); // Z-Achse

// Inserting Row
        db.insert(TABLE_GYRO, null, values);
        db.close(); // Closing database connection
    }

    // Getting one Gyro
    public Gyro getGYRO(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_GYRO, new String[] { KEY_GYRO_ID,
                        KEY_X, KEY_Y,KEY_Z }, KEY_GYRO_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Gyro contactGyro = new Gyro(Integer.parseInt(cursor.getString(0)),
                Double.parseDouble(cursor.getString(1)), Double.parseDouble(cursor.getString(2)), Double.parseDouble(cursor.getString(3)));
// return gyro
        return contactGyro;
    }
    // Getting All Gyro
    public List<Gyro> getAllGYRO() {
        List<Gyro> gyroList = new ArrayList<Gyro>();
// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_GYRO;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Gyro gyro = new Gyro();
                gyro.setId(Integer.parseInt(cursor.getString(0)));
                gyro.setX(Double.parseDouble(cursor.getString(1)));
                gyro.setY(Double.parseDouble(cursor.getString(2)));
                gyro.setZ(Double.parseDouble(cursor.getString(3)));
// Adding contact to list
                gyroList.add(gyro);
            } while (cursor.moveToNext());
        }
// return contact list
        return gyroList;
    }
    // Getting gyro Count
    public int getGyroCount() {
        String countQuery = "SELECT * FROM" + TABLE_GYRO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
// return count
        return cursor.getCount();
    }
    // Updating a gyro
    public int updateGYRO(Gyro gyro) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_X, gyro.getX());
        values.put(KEY_Y, gyro.getY());
        values.put(KEY_Y, gyro.getZ());

// updating row
        return db.update(TABLE_GYRO, values, KEY_GYRO_ID + " = ?",
                new String[]{String.valueOf(gyro.getId())});
    }
    // Deleting a Gyro
    public void deleteGyro(Gyro gyro) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GYRO, KEY_GYRO_ID + " = ?",
                new String[] { String.valueOf(gyro.getId()) });
        db.close();
    }
    //-------------------- DB_LIGHT_Methods-------------------------------------------------------------------------------------------------------------------------------------------------
    // Adding new light
    public void addLIGHT(Light light) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues LIGHT_values = new ContentValues();
        LIGHT_values.put(KEY_LUX, light.getLux()); // Lux
// Inserting Row
        db.insert(TABLE_LIGHT, null, LIGHT_values);
        db.close(); // Closing database connection
    }

    // Getting one Light
    public Light getLIGHT(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LIGHT, new String[] { KEY_LIGHT_ID,
                        KEY_LUX }, KEY_LIGHT_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Light contactLIGHT = new Light(Integer.parseInt(cursor.getString(0)),
                Double.parseDouble(cursor.getString(1)));
// return gps
        return contactLIGHT;
    }

    // Getting All Light----------------------------------MIKA die Methode brauchst du----------------------------------------------------------------------------------------------
    // Die Methode gibt dir eine List mit Light Objekten wieder
    // die kannst du dann in deiner Klasse aufrufen
    // zuerst muss du einen DBHandler erstellen undzwar so -------> DBHandler db = new DBHandler(this);    (Das kommt nach dem Klassen aufruf)
    // z.B.     public class xy extends AppCompatActivity{DBHandler db = new DBHandler(this);   ..............  }
    // Dann erstellst du eine Liste so --------> List<Light> data = db.getAllLIGHT();
    // mit data.get(int index).getLux   bekommse die daten
    public List<Light> getAllLIGHT() {
        List<Light> lightList = new ArrayList<Light>();
// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_LIGHT;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Light light = new Light();
                light.setId(Integer.parseInt(cursor.getString(0)));
                light.setLux(Double.parseDouble(cursor.getString(1)));
// Adding contact to list
                lightList.add(light);
            } while (cursor.moveToNext());
        }
// return contact list
        return lightList;
    }
    // Getting Light Count
    public int getLightCount() {
        String countQuery = "SELECT * FROM" + TABLE_LIGHT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
// return count
        return cursor.getCount();
    }
    // Updating a Light
    public int updateLIGHT(Light light) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Light_values = new ContentValues();
        Light_values.put(KEY_LUX, light.getLux());
// updating row
        return db.update(TABLE_LIGHT, Light_values, KEY_LIGHT_ID + " = ?",
                new String[]{String.valueOf(light.getId())});
    }
    // Deleting a Light
    public void deleteLight(Light light) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LIGHT, KEY_LIGHT_ID + " = ?",
                new String[] { String.valueOf(light.getId()) });
        db.close();
    }
}
