package hackcu.myapplication;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class Gesture2 extends ActionBarActivity {

    private SensorManager mSensorManager;
    private Sensor gSensor;
    private Sensor aSensor;
    private float gravity[] = new float[3];
    private final SensorEventListener mSensorListener = new SensorEventListener(){

        public void onSensorChanged(SensorEvent event) {
            final float alpha = 0.8f;
            float movement[] = new float[3];
            if(event.sensor == gSensor){
                gravity[0] = event.values[0];
                gravity[1] = event.values[1];
                gravity[2] = event.values[2];
            }
            // Isolate the force of gravity with the low-pass filter.
            if(event.sensor == aSensor) {
                movement[0] = event.values[0] - gravity[0];
                movement[1] = event.values[1] - gravity[1];
                movement[2] = event.values[2] - gravity[2];
                if (Math.sqrt(movement[0] * movement[0] + movement[1] * movement[1] + movement[2] * movement[2]) > 10) {
                    if (Math.abs(movement[0]) > Math.abs(movement[1]) && Math.abs(movement[0]) > Math.abs(movement[2])) {
                        if (movement[0] < 0) {
                            Log.v("Gesture", "LEFT");
                        } else {
                            Log.v("Gesture", "RIGHT");
                        }
                    } else if (Math.abs(movement[1]) > Math.abs(movement[0]) && Math.abs(movement[1]) > Math.abs(movement[2])) {
                        if (movement[1] < 0) {
                            Log.v("Gesture", "DOWN");
                        } else {
                            Log.v("Gesture", "DOWN");
                        }
                    } else {
                        if (movement[2] < 0) {
                            Log.v("Gesture", "BACKWARD");
                        } else {
                            Log.v("Gesture", "FORWARD");
                        }
                    }
                }
            }
          }
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY), SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture2);
        gravity[0] = 0;
        gravity[1] = 0;
        gravity[2] = 0;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        aSensor= mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gSensor= mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY), SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        Log.v("Gesture2", "OnCreate");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gesture2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
