package m2rism.myappmeteo;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import m2rism.myappmeteo.adapters.CityAdapter;
import m2rism.myappmeteo.classes.City;
import m2rism.myappmeteo.db.DbCity;
import m2rism.myappmeteo.global.Utils;
import m2rism.myappmeteo.receiver.ReceiverItent;
import m2rism.myappmeteo.service.AppelService;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener{//, LoaderManager.LoaderCallbacks<Cursor> {
    public static List<City> sArrayCity;

    //private DbCity mDb;

    private Intent mServiceIntent;
    private ListView mLVCity;
    private CityAdapter mCityAdapter;
    private ProgressDialog dialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg)	{
            if ( msg.what == 0)
                Toast.makeText(MainActivity.this, "Terminé", Toast.LENGTH_SHORT).show();

        }
    };
    public static final String sURL = "http://www.webservicex.net/globalweather.asmx";

    //private SimpleCursorAdapter mSimpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mDb = new DbCity();
        String[] columns = new String[] {DbCity.CITY_COLUMN_NAME,DbCity.COUNTRY_COLUMN_NAME};
        int[] layoutIds = new int[] {android.R.id.text1,android.R.id.text2};
        /*mSimpleCursorAdapter = new SimpleCursorAdapter(
            getApplicationContext(),
            android.R.layout.simple_list_item_2,
                (Cursor) sArrayCity,
            columns,
            layoutIds,0);*/
        sArrayCity = new ArrayList<City>();
        Cursor lCursor = getContentResolver().query(DbCity.CONTENT_URI,
                new String[]{DbCity.ID_COLUMN_NAME,
                        DbCity.CITY_COLUMN_NAME,
                        DbCity.COUNTRY_COLUMN_NAME,
                        DbCity.VENT_COLUMN_NAME,
                        DbCity.PRESSION_COLUMN_NAME,
                        DbCity.TEMPERATURE_COLUMN_NAME,
                        DbCity.DATE_COLUMN_NAME},
                null, null, null);

        if (lCursor.getCount() == 0) {/* si y a rien dans la data base (! doesDatabaseExist(this,DbCity.DB_NAME) )*/
            //mDb.open();
            Log.i("DATABASE", "THE DATABASE IS EMPTY ");

           // sArrayCity = new ArrayList<City>();
            sArrayCity.add(new City("Brest", "France"));
            sArrayCity.add(new City("Marseille", "France"));
            sArrayCity.add(new City("Montreal", "Canada"));
            sArrayCity.add(new City("Istanbul", "Turkey"));
            sArrayCity.add(new City("Seoul", "Korea"));
            for (City city : sArrayCity) {
                DbCity.addCityIndb(getContentResolver(), city);
            }
            Log.i("DATABASE", "THE DATABASE NOT NOT NOT EXIST");
        } else {
            //mDb.open();
            // sArrayCity = mDb.getAllCities();
            Log.i("DATABASE", "THE DATABASE EXIST EXIST EXIST");

            if (lCursor != null && lCursor.moveToFirst()) {
                do {
                    try {
                        City lCity = new City(lCursor.getString(1), lCursor.getString(2));
                        lCity.setmDirectionVent(lCursor.getString(3));
                        lCity.setmPression(lCursor.getString(4));
                        lCity.setmTemperatureAire(lCursor.getString(5));
                        lCity.setmDernierReleve(lCursor.getString(6));
                        lCity.setmId(lCursor.getInt(0));
                        sArrayCity.add(lCity);
                    } catch (Exception e) {
                        Log.i("EXCEPTION", "/!\\ ERROR : "+e.getMessage());

                    }
                } while (lCursor.moveToNext());
            }
            if(!lCursor.isClosed()) {
                lCursor.close();
            }
        }




        //Sorting
        /*Collections.sort(sArrayCity, new Comparator<City>() {
            @Override
            public int compare(City city1, City city2) {

                return city1.getmNom().compareTo(city2.getmNom());
            }
        });*/

        mCityAdapter = new CityAdapter(getApplicationContext(),sArrayCity);
        mLVCity = (ListView) findViewById(R.id.listCity);

        mLVCity.setAdapter(mCityAdapter);
        //mLVCity.setAdapter(mSimpleCursorAdapter);
        registerForContextMenu(mLVCity);

        mLVCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, CityView.class);
                intent.putExtra("MyCity", sArrayCity.get(position));
                startActivity(intent);
            }
        });
        mLVCity.setOnItemLongClickListener(this);


        ReceiverItent myBroadcastReceiver = new ReceiverItent();
        IntentFilter intentFilter = new IntentFilter(Utils.ACTION_UPDATE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(myBroadcastReceiver, intentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.listCity) {
            menu.add("Supprimer");
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        switch(item.getItemId()){
            case 0:// Qui correspond à "Supprimer"
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

                // Suppression de la ville dans la bdd :
                //mDb.deleteCity(sArrayCity.get(info.position));

                DbCity.deleteCityInDb(getContentResolver(),sArrayCity.get(info.position));

                sArrayCity.remove(info.position);
                mCityAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext()," Suppression : Ok ",Toast.LENGTH_LONG).show();
                break;
        }
        return super.onContextItemSelected(item);

    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    public void addCity(View view){
        Intent lIntent = new Intent(this,AddCityActivity.class);
        startActivity(lIntent);
    }

    public void refreshAction(MenuItem menuItem){
        for (City city : sArrayCity) {
            mServiceIntent = new Intent(this, AppelService.class);
            mServiceIntent.putExtra(Utils.ID,city.getmId());
            mServiceIntent.putExtra(Utils.PAYS,city.getmPays());
            mServiceIntent.putExtra(Utils.VILLE,city.getmNom());
            mServiceIntent.putExtra(Utils.POSITION,sArrayCity.indexOf(city));
            this.startService(mServiceIntent);
        }
        /* 1er TP
        MyTask myTask = new MyTask();
        myTask.execute();*/
    }

    /*@Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
           CursorLoader loader = new CursorLoader(
         this,
         DbCity.CONTENT_URI,
         /*projection* /null,
         /*selection* /null,
         /*selectionArgs* /null,
         /*sortOrder* /null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
       mSimpleCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }*/

    private class MyTask extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(MainActivity.this,"","Veuillez patienter...");

        }
        @Override
        protected Object doInBackground(Object[] params) {

            URL url = null;
            try {
                for (City city : sArrayCity) {
                    url = new URL(sURL + "/GetWeather?CityName=" + city.getmNom() + "&CountryName=" + city.getmPays());
                    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                    XMLResponseHandler xmlResponseHandler = new XMLResponseHandler();
                    List<String> tmp = xmlResponseHandler.handleResponse(httpConn.getInputStream(), "utf-8");
                    city.setmDirectionVent(tmp.get(0));
                    city.setmTemperatureAire(tmp.get(1));
                    city.setmPression(tmp.get(2));
                    city.setmDernierReleve(tmp.get(3));
                    Log.i("TAGTAG", " begin : " + city);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            dialog.dismiss();
            mHandler.sendEmptyMessage(0);
        }
    }
    @Override
    protected void onDestroy(){
        //mDb.close();
        super.onDestroy();
    }

    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }


}
