package com.example.datensammler17;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.id.message;
import static android.R.id.shareText;
import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class Anzeige extends AppCompatActivity implements SensorEventListener {

    //Variablen
    // Accelerometer Daten
    private double xAcc = 0, yAcc = 0, zAcc = 0;
    private float[] gravity = {9.81f, 9.81f, 9.81f};
    private float[] linear_acceleration = new float[3];
    private LineGraphSeries<DataPoint>[] series_acc = new LineGraphSeries[3];
    //Gyroscope Daten
    private double xGyro = 0, yGyro = 0, zGyro = 0;
    private LineGraphSeries<DataPoint>[] series_gyro = new LineGraphSeries[3];
    private final float[] mAccelerometerReading = new float[3]; //Eingabe Accelerometer
    private final float[] mMagnetometerReading = new float[3];  //Eingabe Magnetometer
    private final float[] mRotationMatrix = new float[9];       //Rotationsmatrix
    private final float[] mOrientationAngles = new float[3];    //Orientationswinkel
    //Lichtsensor Daten
    private double light = 0;
    private LineGraphSeries<DataPoint> series_light;
    //Proximitysensor Daten
    private double prox = 0;
    private LineGraphSeries<DataPoint> series_prox;
    //Sensoren
    private Sensor sensor_acc;
    private Sensor sensor_mag;
    private Sensor sensor_light;
    private Sensor sensor_prox;
    //Sensormanager
    private SensorManager sm;
    // GraphView
    private Handler handle = new Handler();
    private Runnable runs;
    private int time;   // Zeit-Achse
    //Speicherung
    private StringBuffer daten = new StringBuffer("");
    private TimerTask tt = null;
    private Timer t = null;
    private long interval;

    private SharedPreferences sharedPref;


    // Methoden
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anzeige);

        //set preferences to default at first start
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);


        //Bildschirm angeschaltet halten
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Sensoren definieren
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor_acc = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensor_light = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensor_mag = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensor_prox = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        // Sensorlistener registrieren
        sm.registerListener(this, sensor_acc, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, sensor_light, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, sensor_mag, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, sensor_prox, SensorManager.SENSOR_DELAY_NORMAL);

        // Liniendiagramm des Gyroscope
        GraphView graph_gyro = (GraphView) findViewById(R.id.graph_gyro);
        series_gyro[0] = new LineGraphSeries<>();
        series_gyro[0].setColor(Color.BLUE);
        graph_gyro.addSeries(series_gyro[0]);
        series_gyro[1] = new LineGraphSeries<>();
        series_gyro[1].setColor(Color.GREEN);
        graph_gyro.addSeries(series_gyro[1]);
        series_gyro[2] = new LineGraphSeries<>();
        series_gyro[2].setColor(Color.RED);
        graph_gyro.addSeries(series_gyro[2]);
        // festes Viewport definieren
        graph_gyro.getViewport().setXAxisBoundsManual(true);
        graph_gyro.getViewport().setMinX(0);
        graph_gyro.getViewport().setMaxX(10);
        graph_gyro.getViewport().setScrollable(true);
        // Achsenbeschriftung
        graph_gyro.getGridLabelRenderer().setVerticalAxisTitle("Rotation in rad/s");
        graph_gyro.getGridLabelRenderer().setHorizontalAxisTitle("Zeit in s");
        // Legende erstellen
        series_gyro[0].setTitle("x");
        series_gyro[1].setTitle("y");
        series_gyro[2].setTitle("z");
        graph_gyro.getLegendRenderer().setVisible(true);
        graph_gyro.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        // Liniendiagramm des Accelerometers
        GraphView graph_acc = (GraphView) findViewById(R.id.graph_acc);
        series_acc[0] = new LineGraphSeries<>();
        series_acc[0].setColor(Color.BLUE);
        graph_acc.addSeries(series_acc[0]);
        series_acc[1] = new LineGraphSeries<>();
        series_acc[1].setColor(Color.GREEN);
        graph_acc.addSeries(series_acc[1]);
        series_acc[2] = new LineGraphSeries<>();
        series_acc[2].setColor(Color.RED);
        graph_acc.addSeries(series_acc[2]);
        // festes Viewport definieren
        graph_acc.getViewport().setXAxisBoundsManual(true);
        graph_acc.getViewport().setMinX(0);
        graph_acc.getViewport().setMaxX(10);
        graph_acc.getViewport().setScrollable(true);
        // Achsenbeschriftung
        graph_acc.getGridLabelRenderer().setVerticalAxisTitle("Beschleunigung in m/s²");
        graph_acc.getGridLabelRenderer().setHorizontalAxisTitle("Zeit in s");
        // Legende erstellen
        series_acc[0].setTitle("x");
        series_acc[1].setTitle("y");
        series_acc[2].setTitle("z");
        graph_acc.getLegendRenderer().setVisible(true);
        graph_acc.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        //  Liniendiagramm des Lichtsensors
        GraphView graph_light = (GraphView) findViewById(R.id.graph_light);
        series_light = new LineGraphSeries<>();
        series_light.setColor(Color.YELLOW);
        graph_light.addSeries(series_light);
        // festes Viewport definieren
        graph_light.getViewport().setXAxisBoundsManual(true);
        graph_light.getViewport().setMinX(0);
        graph_light.getViewport().setMaxX(10);
        graph_light.getViewport().setScrollable(true);
        // Achsenbeschriftung
        graph_light.getGridLabelRenderer().setVerticalAxisTitle("Helligkeit in lx");
        graph_light.getGridLabelRenderer().setHorizontalAxisTitle("Zeit in s");

        // Liniendiagramm des Proximitysensors
        GraphView graph_prox = (GraphView) findViewById(R.id.graph_prox);
        series_prox = new LineGraphSeries<>();
        series_prox.setColor(Color.BLACK);
        graph_prox.addSeries(series_prox);
        // festes Viewport definieren
        graph_prox.getViewport().setXAxisBoundsManual(true);
        graph_prox.getViewport().setMinX(0);
        graph_prox.getViewport().setMaxX(10);
        graph_prox.getViewport().setScrollable(true);
        // Achsenbeschriftung
        graph_prox.getGridLabelRenderer().setVerticalAxisTitle("Abstand in cm....Edgar wieso bist du nicht da !!!!");
        graph_prox.getGridLabelRenderer().setHorizontalAxisTitle("Zeit in s");

        //Speicherfunktion einrichten
        // Edgar wieso bist du nicht da !!!!
        //t.scheduleAtFixedRate(tt,1000,instance.getIntervall());
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not used yet
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        // Accelerometer Event
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Berechnungen
            final float alpha = 0.8f;
            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];
            // Ausgabe setzen
            xAcc = linear_acceleration[0];
            yAcc = linear_acceleration[1];
            zAcc = linear_acceleration[2];
            System.arraycopy(event.values, 0, mAccelerometerReading,
                    0, mAccelerometerReading.length);
            // Magnetfeldsensor Event
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mMagnetometerReading,
                    0, mMagnetometerReading.length);
            updateOrientationAngles();
            xGyro = mOrientationAngles[0];
            yGyro = mOrientationAngles[1];
            zGyro = mOrientationAngles[2];
            // Lichtsensor Event
        } else if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            light = event.values[0];
            // Proximitysensor Event
        } else if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            prox = event.values[0];
        }
    }


    //Berechnung der Orientierungswinkel
    public void updateOrientationAngles() {
        SensorManager.getRotationMatrix(mRotationMatrix, null, mAccelerometerReading, mMagnetometerReading);
        SensorManager.getOrientation(mRotationMatrix, mOrientationAngles);
    }

    public void setTimer() {
        t = new Timer();
        interval = Long.parseLong(sharedPref.getString(getString(R.string.pref_interval),getString(R.string.pref_interval_default)));
        tt = new TimerTask() {
            @Override
            public void run() {
                SimpleDateFormat s = new SimpleDateFormat("hh:mm:ss.SSS");
                String time = s.format(new Date());
                String gyrotxt = "", acctxt = "", gpstxt = "", lighttxt = "", proxtxt = "";

                boolean isGyroOn = sharedPref.getBoolean(getString(R.string.pref_gyro), true);
                boolean isAccOn = sharedPref.getBoolean(getString(R.string.pref_acc), true);
                boolean isHellOn = sharedPref.getBoolean(getString(R.string.pref_hell), true);
                boolean isProxOn = sharedPref.getBoolean(getString(R.string.pref_prox), true);


                if (isGyroOn)
                    gyrotxt = "Gyrometer: " + String.format("%.2f", xGyro) + " "
                            + String.format("%.2f", yGyro) + " " + String.format("%.2f", zGyro) + "\n";
                if (isAccOn)
                    acctxt = "Beschleunigung: " + String.format("%.2f", xAcc) + " "
                            + String.format("%.2f", yAcc) + " " + String.format("%.2f", zAcc) + "\n";
                if (isHellOn)
                    lighttxt = "Helligkeit: " + light + "\n";
                if (isProxOn)
                    proxtxt = "Abstand: " + prox + "\n";
                daten.append("Zeit: " + time + "\n" + gyrotxt + acctxt + gpstxt + lighttxt + proxtxt + "\n");
            }
        };
        t.scheduleAtFixedRate(tt, 1000, interval);
    }


    //Resume
    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        sm.registerListener(this, sensor_acc, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, sensor_mag, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, sensor_light, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, sensor_prox, SensorManager.SENSOR_DELAY_NORMAL);
        runs = new Runnable() {
            @Override
            public void run() {
                time++;
                // gyro graph laeuft
                series_gyro[0].appendData(new DataPoint(time, xGyro), true, 50);
                series_gyro[1].appendData(new DataPoint(time, yGyro), true, 50);
                series_gyro[2].appendData(new DataPoint(time, zGyro), true, 50);
                // acc graph laeuft
                series_acc[0].appendData(new DataPoint(time, xAcc), true, 50);
                series_acc[1].appendData(new DataPoint(time, yAcc), true, 50);
                series_acc[2].appendData(new DataPoint(time, zAcc), true, 50);
                // light graph laeuft
                series_light.appendData(new DataPoint(time, light), true, 50);
                // proximity graph laeuft
                series_prox.appendData(new DataPoint(time, prox), true, 50);
                handle.postDelayed(this, 1000);
            }
        };
        setTimer();
        handle.postDelayed(runs, 1000);
    }


    //Pause
    @Override
    protected void onPause() {
        super.onPause();
        t.cancel();
        sm.unregisterListener(this);
        handle.removeCallbacks(runs);
    }


    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }


    //Button Stop
    public void stopRec(View view) {
        //Timer abbrechen
        t.cancel();
        //Listener stoppen
        sm.unregisterListener(this);
        handle.removeCallbacks(runs);
        //Speicherung der Daten mit Timestamp als Filename
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = s.format(new Date());
        //Nur wenn Rechte vergeben sind
        if (isExternalStorageWritable()) {
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Datensammler");
            if (!dir.mkdirs()) {
                System.out.println("Ordner konnte nicht erstellt werden");
            }
            File file = new File(dir, time + ".txt");
            try {
                FileOutputStream stream = new FileOutputStream(file);
                stream.write(daten.toString().getBytes());
                stream.flush();
                stream.close();
                System.out.println("Speichern erfolgreich");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
