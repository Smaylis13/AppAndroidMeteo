package m2rism.myappmeteo.global;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import m2rism.myappmeteo.classes.City;
import m2rism.myappmeteo.db.DbCity;

/**
 * Created by lyamsi on 17/10/15.
 */
public class Utils {
    public static final String ACTION_UPDATE = "m2rism.myappmeteo.UPDATE";
    public static final String EXTRA_KEY_UPDATE = "EXTRA_UPDATE";
    public static final String POSITION = "POSITION";
    public static final String PAYS = "PAYS";
    public static final String VILLE = "VILLE";
    public static final String ID = "ID";

    /**
     *
     * @param pCity
     * @return transforme pCity en ContentValues
     */
    public static ContentValues cityValues(City pCity){
        ContentValues lValues = new ContentValues();


        lValues.put(DbCity.CITY_COLUMN_NAME, pCity.getmNom());
        lValues.put(DbCity.COUNTRY_COLUMN_NAME,   pCity.getmPays() );
        lValues.put(DbCity.VENT_COLUMN_NAME,     pCity.getmDirectionVent());
        lValues.put(DbCity.TEMPERATURE_COLUMN_NAME, pCity.getmTemperatureAire());
        lValues.put(DbCity.DATE_COLUMN_NAME, pCity.getmDernierReleve());
        lValues.put(DbCity.PRESSION_COLUMN_NAME, pCity.getmPression());

        return lValues;
    }

    /**
     *
     * @param pCity
     * @return chemin uri bdd
     */
    public static Uri cityUri(City pCity){
        return Uri.parse(DbCity.URL+"/"+pCity.getmPays()+"/"+pCity.getmNom());
    }


}
