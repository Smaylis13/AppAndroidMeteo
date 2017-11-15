package m2rism.myappmeteo.service;



import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import m2rism.myappmeteo.MainActivity;
import m2rism.myappmeteo.XMLResponseHandler;
import m2rism.myappmeteo.classes.City;
import m2rism.myappmeteo.global.Utils;

public class AppelService extends IntentService {
    private static final String NAMESPACE = "http://www.webserviceX.NET";
    private static final String URL = "http://www.webservicex.net/globalweather.asmx";
    private static final String SOAP_ACTION = "http://www.webserviceX.NET/GetWeather";
    private static final String METHOD_NAME = "GetWeather";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AppelService(String name) {
        super(name);
    }
    public AppelService(){
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        //String dataString = intent.getDataString();
        City lCity = new City(intent.getStringExtra(Utils.VILLE),intent.getStringExtra(Utils.PAYS));
        lCity.setmId(intent.getIntExtra(Utils.ID,0));


        int index = intent.getIntExtra(Utils.POSITION,0);
// waiting...
        try {
            URL url = new URL(MainActivity.sURL + "/GetWeather?CityName=" + lCity.getmNom() + "&CountryName=" + lCity.getmPays());

            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            XMLResponseHandler xmlResponseHandler = new XMLResponseHandler();
            List<String> tmp = xmlResponseHandler.handleResponse(httpConn.getInputStream(), "utf-8");

            if (!(tmp.isEmpty())) {
                lCity.setmDirectionVent(tmp.get(0));
                lCity.setmTemperatureAire(tmp.get(1));
                lCity.setmPression(tmp.get(2));
                lCity.setmDernierReleve(tmp.get(3));

            }
          //  MainActivity.sArrayCity.set(index, lCity);

           Intent intent1 = new Intent();
           intent1.setAction(Utils.ACTION_UPDATE);
           intent1.addCategory(Intent.CATEGORY_DEFAULT);
           intent1.putExtra(Utils.EXTRA_KEY_UPDATE, lCity);
            intent1.putExtra(Utils.POSITION,index);
           sendBroadcast(intent1);


        } catch (IOException e) {
            Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(AppelService.this.getApplicationContext(), "No internet access...", Toast.LENGTH_LONG).show();
            }
        });
        }

   }

}