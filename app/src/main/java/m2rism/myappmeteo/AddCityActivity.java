package m2rism.myappmeteo;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import m2rism.myappmeteo.classes.City;
import m2rism.myappmeteo.db.DbCity;

public class AddCityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_city, menu);
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

    public void sauverCity(View view){
        EditText lVille = (EditText) findViewById(R.id.eVille);
        EditText lPays = (EditText) findViewById(R.id.ePays);
        City lCity = new City(lVille.getText().toString(),lPays.getText().toString());
        for(City city : MainActivity.sArrayCity){
            if(lCity.equals(city)){
                Toast.makeText(getApplicationContext(),"Ville existe déjà!",Toast.LENGTH_LONG).show();
                finish();
            }
        }
        MainActivity.sArrayCity.add(lCity);

        DbCity.addCityIndb(getContentResolver(),lCity);
        /*
        DbCity lDb = new DbCity(this);
        lDb.open();
        lDb.addCity(lCity);

        lDb.close();*/

        finish();
    }
}
