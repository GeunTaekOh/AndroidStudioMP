package com.taek_aaa.myapplication;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    public static int count = 0;
    TextView textView;
    Button resetBtn;
    public long lastTime;
    public float speed, lastX,lastY,lastZ;
    public float x,y,z;

    public SensorManager sensorManager;
    public Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.tv);
        resetBtn = (Button)findViewById(R.id.resetButton);
        textView.setText("You walked "+count+" Steps!!");
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count=0;
                textView.setText("You walked "+count+" Steps!!");
            }
        });


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
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            long currentTime = System.currentTimeMillis();
            long gapOfTime = (currentTime-lastTime);
            if(gapOfTime>100){
                lastTime=currentTime;
                x=sensorEvent.values[SensorManager.DATA_X];
                y=sensorEvent.values[SensorManager.DATA_Y];
                z=sensorEvent.values[SensorManager.DATA_Z];

                speed=Math.abs(x+y+z - lastX-lastY-lastZ)/gapOfTime*10000;

                if(speed>900){
                    textView.setText("You walked "+(++count)+" Steps!!");
                }

                lastX = sensorEvent.values[SensorManager.DATA_X];
                lastY = sensorEvent.values[SensorManager.DATA_Y];
                lastZ = sensorEvent.values[SensorManager.DATA_Z];
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
