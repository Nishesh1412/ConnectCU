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
    private float timestamp;
    private final float[] deltaRotationVector = new float[4];
    private static final float NS2S = 1.0f / 1000000000.0f;
    private float currentPos[] = new float[3];

    private final SensorEventListener mSensorListener = new SensorEventListener(){
        public void onSensorChanged(SensorEvent event) {
            final float alpha = 0.8f;
            final float dT = (event.timestamp - timestamp) * NS2S;
            float angVelocity[] = new float[3];

            angVelocity[0] = event.values[0];
            angVelocity[1] = event.values[1];
            angVelocity[2] = event.values[2];
            double omegaMagnitude = Math.sqrt(angVelocity[0] * angVelocity[0] + angVelocity[1] * angVelocity[1] + angVelocity[2] * angVelocity[2]);
            /*
            angVelocity[0] /= omegaMagnitude;
            angVelocity[1] /= omegaMagnitude;
            angVelocity[2] /= omegaMagnitude;*/
            double thetaOverTwo = omegaMagnitude * dT / 2.0;
            float sinThetaOverTwo = (float)Math.sin(thetaOverTwo);
            float cosThetaOverTwo = (float)Math.cos(thetaOverTwo);
            deltaRotationVector[0] = sinThetaOverTwo * angVelocity[0];
            deltaRotationVector[1] = sinThetaOverTwo * angVelocity[1];
            deltaRotationVector[2] = sinThetaOverTwo * angVelocity[2];
            deltaRotationVector[3] = cosThetaOverTwo;
            float[] deltaRotationMatrix = new float[9];
            SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
            currentPos[0] = currentPos[0] * deltaRotationMatrix[0] + currentPos[0] * deltaRotationMatrix[1] + currentPos[0] * deltaRotationMatrix[2] + deltaRotationVector[0];
            currentPos[1] = currentPos[1] * deltaRotationMatrix[3] + currentPos[1] * deltaRotationMatrix[4] + currentPos[1] * deltaRotationMatrix[5] + deltaRotationVector[1];
            currentPos[2] = currentPos[2] * deltaRotationMatrix[6] + currentPos[2] * deltaRotationMatrix[7] + currentPos[2] * deltaRotationMatrix[8] + deltaRotationVector[2];
            if (Math.sqrt(deltaRotationVector[0] * deltaRotationVector[0] + deltaRotationVector[1] * deltaRotationVector[1] + deltaRotationVector[2] * deltaRotationVector[2]) > .5) {
                    //Log.d("deltaRotationVector", "Value " + deltaRotationVector[0]);
                    //Log.d("deltaRotationVector", "Value " + deltaRotationVector[1]);
                    //Log.d("deltaRotationVector", "Value " + deltaRotationVector[2]);
                    if (Math.abs(deltaRotationVector[0]) > Math.abs(deltaRotationVector[1]) && Math.abs(deltaRotationVector[0]) > Math.abs(deltaRotationVector[2])) {
                        if (deltaRotationVector[0] < 0) {
                            Log.v("Gesture", "Lean Away");
                        } else {
                            Log.v("Gesture", "Lean Towards");
                        }
                    } else if (Math.abs(deltaRotationVector[1]) > Math.abs(deltaRotationVector[0]) && Math.abs(deltaRotationVector[1]) > Math.abs(deltaRotationVector[2])) {
                        if (deltaRotationVector[1] < 0) {
                            Log.v("Gesture", "Twist Left");
                        } else {
                            Log.v("Gesture", "Twist Right");
                        }
                    } else {
                        if (deltaRotationVector[2] < 0) {
                            Log.v("Gesture", "Lean Right");
                        } else {
                            Log.v("Gesture", "Lean Left");
                        }
                }
            }
            timestamp = event.timestamp;
        }
        /*
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
                if (Math.sqrt(movement[0] * movement[0] + movement[1] * movement[1] + movement[2] * movement[2]) > 20 || change == false) {
                    change = !change;
                    if (change == true) {
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
          }*/
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME);
        //mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY), SensorManager.SENSOR_DELAY_GAME);
        //mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
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
        currentPos[0] = 1;
        currentPos[1] = 1;
        currentPos[2] = 1;
        timestamp = System.currentTimeMillis() * 1000000;
        //gravity[0] = 0;
        //gravity[1] = 0;
        //gravity[2] = 0;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME);
        //aSensor= mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //gSensor= mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        //mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY), SensorManager.SENSOR_DELAY_UI);
        //mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
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
