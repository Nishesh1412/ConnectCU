package hackcu.myapplication;

import android.app.Application;
import android.bluetooth.BluetoothSocket;

/**
 * Created by Alex on 4/12/2015.
 */
public class MyApplication extends Application {
    private BluetoothSocket btSocket;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public BluetoothSocket getSocket() {
        return btSocket;
    }

    public void setSocket(BluetoothSocket socket) {
        this.btSocket = socket;
    }
}