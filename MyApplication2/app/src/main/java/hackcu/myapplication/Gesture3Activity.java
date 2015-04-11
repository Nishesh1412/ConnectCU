package hackcu.myapplication;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import hackcu.myapplication.R;

public class Gesture3Activity extends ActionBarActivity {
    boolean appear = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gesture3, menu);
        return true;
    }

    /*public boolean appear(){
        ImageView imgView = (ImageView)findViewById(R.id.spark_1);
        imgView.setVisibility(View.INVISIBLE);
        return false;
    }*/

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
