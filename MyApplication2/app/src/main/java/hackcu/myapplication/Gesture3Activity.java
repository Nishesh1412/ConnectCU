package hackcu.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import hackcu.myapplication.R;

public class Gesture3Activity extends ActionBarActivity {
    boolean appear = false;
    private SensorManager mSensorManager;
    private float timestamp;
    private final float[] deltaRotationVector = new float[4];
    private static final float NS2S = 1.0f / 1000000000.0f;
    private float currentPos[] = new float[3];
    private int currentPic = 0;
    Handler mHandler = new Handler();

    private final SensorEventListener mSensorListener = new SensorEventListener(){
        public void onSensorChanged(SensorEvent event) {
            final float alpha = 0.8f;
            final float dT = (event.timestamp - timestamp) * NS2S;
            float angVelocity[] = new float[3];

            angVelocity[0] = event.values[0];
            angVelocity[1] = event.values[1];
            angVelocity[2] = event.values[2];
            double omegaMagnitude = Math.sqrt(angVelocity[0] * angVelocity[0] + angVelocity[1] * angVelocity[1] + angVelocity[2] * angVelocity[2]);
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
                        setSpark(R.id.shield_2);
                        Log.v("Gesture", "Lean Away");
                    } else {
                        setSpark(R.id.shield_1);
                        Log.v("Gesture", "Lean Towards");
                    }
                } else if (Math.abs(deltaRotationVector[1]) > Math.abs(deltaRotationVector[0]) && Math.abs(deltaRotationVector[1]) > Math.abs(deltaRotationVector[2])) {
                    if (deltaRotationVector[1] < 0) {
                        setSpark(R.id.spark_2);
                        Log.v("Gesture", "Twist Left");
                    } else {
                        setSpark(R.id.spark_3);
                        Log.v("Gesture", "Twist Right");
                    }
                } else {
                    if (deltaRotationVector[2] < 0) {
                        setSpark(R.id.spark_4);
                        Log.v("Gesture", "Lean Right");
                    } else {
                        setSpark(R.id.spark_1);
                        Log.v("Gesture", "Lean Left");
                    }
                }
            }
            timestamp = event.timestamp;
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture3);
        //int id = t.getResourceId(R.styleable.Viewee_linkedView, 0);
        currentPos[0] = 1;
        currentPos[1] = 1;
        currentPos[2] = 1;
        timestamp = System.currentTimeMillis() * 1000000;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME);
        Log.v("Gesture2", "OnCreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    /*public boolean appear(){
        ImageView imgView = (ImageView)findViewById(R.id.spark_1);
        imgView.setVisibility(View.INVISIBLE);
        return false;
    }*/


    public void setSpark(int pic) {
        Log.v("SetSpark", "entered");
        if(currentPic != pic) {
            ImageView imgView = (ImageView) findViewById(pic);
            View View = findViewById(R.id.wand_trans);
            if(currentPic!= 0) {
                ImageView currImgView = (ImageView) findViewById(currentPic);
                currImgView.setVisibility(View.INVISIBLE);
            }
            //imgView.setVisibility(View.INVISIBLE);
            currentPic = pic;
            imgView.setVisibility(View.VISIBLE);
        }
    }
    /*
    public void sparkOnClick(View View){
        ImageView imgView = (ImageView)findViewById(R.id.shield_2);
        if (appear == false){
            imgView.setVisibility(View.VISIBLE);
            appear = true;
        }
        else{
            imgView.setVisibility(View.INVISIBLE);
            appear = false;
        }
    }
    */

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
