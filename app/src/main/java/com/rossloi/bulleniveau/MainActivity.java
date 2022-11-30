package com.rossloi.bulleniveau;

import android.annotation.SuppressLint;
import android.content.Loader;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SensorManager mgr;
    private TextView TV_axeX;
    private TextView TV_axeY;
    private Sensor gravitySensor;
    private ImageView IV_Carre;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation
        IV_Carre = findViewById(R.id.carre);
        mgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        gravitySensor = mgr.getDefaultSensor(Sensor.TYPE_GRAVITY);
        mgr.registerListener(gravitySensorListener, gravitySensor, mgr.SENSOR_DELAY_FASTEST);
    }

    // Création d'un listener
    private SensorEventListener gravitySensorListener = new SensorEventListener() {

        /**
         * transmet les informations de chaque changement des valeurs du capteurs
         * @param sensorEvent Valeur du capteur
         */
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            // Récupération des valeurs
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];

            // Obtenir la position
            float posY = IV_Carre.getY();
            float posX = IV_Carre.getX();

            // Modifier la position
            IV_Carre.setX(posX - x);
            IV_Carre.setY(posY + y);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mgr.registerListener(gravitySensorListener, gravitySensor, mgr.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mgr.unregisterListener(gravitySensorListener);
    }
}