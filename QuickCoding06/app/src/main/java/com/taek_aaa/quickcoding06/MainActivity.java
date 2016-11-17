package com.taek_aaa.quickcoding06;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    public static int count = 0;
    TextView textView;
    Button buttonReset;
    private long lastTime;
    private float speed, lastX, lastY, lastZ;
    private float x, y, z;
    private SensorManager sensorManager;
    private Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        textView = (TextView) findViewById(R.id.text_view);
        buttonReset = (Button) findViewById(R.id.resetButton);
        textView.setText("You Walked " + count + " Steps!!!");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (sensor != null)
            sensorManager.registerListener(this, sensor, sensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (sensorManager != null)
            sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            long gabOfTime = (currentTime - lastTime);
            if (gabOfTime > 100) {
                lastTime = currentTime;
                x = event.values[SensorManager.DATA_X];
                y = event.values[SensorManager.DATA_Y];
                z = event.values[SensorManager.DATA_Z];

                speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime * 10000;

                if (speed > 800) {
                    textView.setText("You Walked " + (++count) + " Steps!!!");
                }

                lastX = event.values[SensorManager.DATA_X];
                lastY = event.values[SensorManager.DATA_Y];
                lastZ = event.values[SensorManager.DATA_Z];
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onClick_Reset(View v) {
        count = 0;
        textView.setText("You Walked " + count + " Steps!!!");
    }
}
