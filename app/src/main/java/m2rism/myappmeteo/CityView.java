package m2rism.myappmeteo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import m2rism.myappmeteo.classes.City;

public class CityView extends AppCompatActivity {

    private TextView mPays;
    private TextView mVille;
    private TextView mVent;
    private TextView mTemperature;
    private TextView mPression;
    private TextView mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_view);
        Intent intent = getIntent();
        City city = (City) intent.getSerializableExtra("MyCity");
        mVille          = (TextView) findViewById(R.id.ville);          mVille.setText(city.getmNom());
        mPays           = (TextView) findViewById(R.id.pays);           mPays.setText(city.getmPays());
        mVent           = (TextView) findViewById(R.id.vent);           mVent.setText(city.getmDirectionVent());
        mPression       = (TextView) findViewById(R.id.pression);       mPression.setText(city.getmPression());
        mTemperature    = (TextView) findViewById(R.id.temperature);    mTemperature.setText(city.getmTemperatureAire());
        mDate           = (TextView) findViewById(R.id.date);           mDate.setText(city.getmDernierReleve());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_city_view, menu);
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
