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
    private Sensor gravitySensor;
    private ImageView IV_Carre;
    private ImageView IV_CarreCentral;
    private int Screen_Left, Screen_Right, Screen_Top, Screen_Bottom;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation
        IV_Carre = findViewById(R.id.carre);
        IV_CarreCentral = findViewById(R.id.carreCentral);
        mgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        gravitySensor = mgr.getDefaultSensor(Sensor.TYPE_GRAVITY);
        mgr.registerListener(gravitySensorListener, gravitySensor, mgr.SENSOR_DELAY_FASTEST);
        Screen_Top = 0;
        Screen_Left = 0;
        Screen_Right = this.getWindowManager().getDefaultDisplay().getWidth();
        Screen_Bottom = this.getWindowManager().getDefaultDisplay().getHeight();
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
            float posCarreY = IV_Carre.getY();
            float posCarreX = IV_Carre.getX();
            float posCarreCentralY = IV_CarreCentral.getY();
            float posCarreCentralX = IV_CarreCentral.getX();

            // Modifier la position du carré
            IV_Carre.setX(posCarreX - x * 2);
            IV_Carre.setY(posCarreY + y * 2);

            // Détection de la partie inférieure de l'écran
            if (posCarreY > Screen_Bottom - (IV_Carre.getHeight() + 100))
                IV_Carre.setY((float) (posCarreY - 0.4));

            // Détection de la partie supérieure de l'écran
            if (posCarreY < Screen_Top)
                IV_Carre.setY((float) (posCarreY + 0.4));

            // Détection de la partie gauche de l'écran
            if (posCarreX < Screen_Left)
                IV_Carre.setX((float) (posCarreX + 0.4));

            // Détection de la partie droite de l'écran
            if (posCarreX > Screen_Right - IV_Carre.getWidth())
                IV_Carre.setX((float) (posCarreX - 0.4));

            /**
             * Test pour la partie supérieure et inférieure du carré central
             */
            if ((posCarreY + IV_Carre.getHeight()) >= posCarreCentralY
                    && posCarreX + IV_Carre.getWidth() > posCarreCentralX
                    && posCarreX < (posCarreCentralX + IV_CarreCentral.getWidth())
                    && posCarreY < (posCarreCentralY + 30)
            ) {
                System.out.println("Collisions en haut");
                IV_Carre.setY((float) (posCarreY - 0.4));

            } else if (posCarreY <= (posCarreCentralY + (IV_CarreCentral.getHeight()))
                    && posCarreX + IV_Carre.getWidth() > posCarreCentralX
                    && posCarreX < (posCarreCentralX + IV_CarreCentral.getWidth())
                    && posCarreY + IV_Carre.getHeight() > posCarreCentralY
            ) {
                System.out.println("Collisions en bas");
                IV_Carre.setY((float) (posCarreY + 0.4));
            }

            if ((posCarreX + IV_Carre.getWidth()) >= posCarreCentralX
                    && posCarreY + IV_Carre.getHeight() > posCarreCentralY
                    && posCarreY < (posCarreCentralY + IV_CarreCentral.getHeight())
                    && posCarreX < (posCarreCentralX  -30)
            ) {
                System.out.println("Collisions a gauche");
                IV_Carre.setX((float) (posCarreX - 0.4));

            } else if (posCarreX <= (posCarreCentralX + (IV_CarreCentral.getWidth()))
                    && posCarreY + IV_Carre.getHeight() > posCarreCentralY
                    && posCarreY < (posCarreCentralY + IV_CarreCentral.getHeight())
                    && posCarreX + IV_Carre.getWidth() > posCarreCentralX
            ) {
                System.out.println("Collisions a droite");
                IV_Carre.setX((float) (posCarreX + 0.4));
            }
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