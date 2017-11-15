package m2rism.myappmeteo.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import m2rism.myappmeteo.classes.City;
import m2rism.myappmeteo.global.Utils;

/**
 * Created by lyamsi on 18/10/15.
 */
public class DbCity extends ContentProvider {



    private static final String AUTHORITY = "m2rism.myappmeteo.provider.wheather";
    public static final String URL = "content://" + AUTHORITY + "/wheather";
    // URI content provider, Elle sera utilisée pour accéder au ContentProvider
    public static final Uri CONTENT_URI = Uri.parse(URL);

    // Creates a UriMatcher object.
    private static final UriMatcher sUriMatcher;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, "wheather",   1);
        sUriMatcher.addURI(AUTHORITY, "wheather/*/*", 2);
    }

    public static final String TABLE_NAME = "city";
    public static final String ID_COLUMN_NAME = "_id";
    public static final String CITY_COLUMN_NAME = "ville";
    public static final String COUNTRY_COLUMN_NAME = "pays";
    public static final String VENT_COLUMN_NAME = "vent";
    public static final String PRESSION_COLUMN_NAME = "pression";
    public static final String TEMPERATURE_COLUMN_NAME = "temperature";
    public static final String DATE_COLUMN_NAME = "date";
    public static final String DB_NAME = "db_name.db";

    private DbHelper mDbHelper;
	private SQLiteDatabase mDb;
    private Context mContext;

    @Override
    public boolean onCreate() {
        mContext = getContext();
        mDbHelper = new DbHelper(mContext,TABLE_NAME, null/*factory*/, 1/*version*/);

        mDb = mDbHelper.getWritableDatabase();


        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

		SQLiteDatabase db = mDbHelper.getReadableDatabase();
        switch (sUriMatcher.match(uri)){
            case 1:
                return db.query(TABLE_NAME,
							    projection, selection, selectionArgs, null, null,
							    sortOrder);
            case 2:

                return db.query(TABLE_NAME,
                        projection, ID_COLUMN_NAME + "=" + uri.getPathSegments().get(1), null, null, null,
                        null);
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
		}

    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            case 1:

                return ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+AUTHORITY;

            case 2:

                return "vnd.android.cursor.item/"+AUTHORITY;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /**      Add a new City     **/

        long lRowID = mDb.insert(TABLE_NAME, null, values);

        if (lRowID > 0){
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, lRowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        try {
            throw new SQLException("Failed to add a city into " + uri);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; //Failed to add

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int lCount;
        switch (sUriMatcher.match(uri)){
            case 1:
                lCount = mDb.delete(TABLE_NAME, selection, selectionArgs);
            break;

            case 2:
                lCount = mDb.delete(TABLE_NAME,
                        CITY_COLUMN_NAME + " = '" + uri.getPathSegments().get(1) + "' AND " +
                        COUNTRY_COLUMN_NAME + " = '" + uri.getPathSegments().get(2) +"'", selectionArgs);
            break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return lCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int count;
        switch (sUriMatcher.match(uri)){
            case 1:
                count = mDb.update(TABLE_NAME, values, selection, selectionArgs);
            break;

            case 2:
                count = mDb.update(TABLE_NAME, values,
                        CITY_COLUMN_NAME + " = '" + uri.getPathSegments().get(1) + "' AND " +
                        COUNTRY_COLUMN_NAME + " = '" + uri.getPathSegments().get(2) +"'", selectionArgs);

                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    private class DbHelper extends SQLiteOpenHelper {




        public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, DB_NAME, factory, version);
            //super(context, name, factory, version);

        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table " + TABLE_NAME + "("
                    + ID_COLUMN_NAME + " integer primary key autoincrement, "
                    + CITY_COLUMN_NAME + " text not null, "
                    + COUNTRY_COLUMN_NAME + " text not null, "
                    + VENT_COLUMN_NAME + " text, "
                    + PRESSION_COLUMN_NAME + " text, "
                    + TEMPERATURE_COLUMN_NAME + " text, "
                    + DATE_COLUMN_NAME + " text,"
                    + "UNIQUE ("+CITY_COLUMN_NAME + ","+COUNTRY_COLUMN_NAME +") );");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

  //  public DbCity(Context pContext) {
    //    mDbHelper = new DbHelper(pContext, TABLE_NAME, null/*factory*/, 1/*version*/);
    //    mContext = pContext;
    //}

    /*public DbCity open(){
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }*/
    public void close(){
		mDb.close();
	}



    private City cursorToCity(Cursor pCursor){
        City lCity = null;
        if (pCursor != null){
            lCity = new City(pCursor.getString(1),pCursor.getString(2));
            lCity.setmDirectionVent(pCursor.getString(3));
            lCity.setmPression(pCursor.getString(4));
            lCity.setmTemperatureAire(pCursor.getString(5));
            lCity.setmDernierReleve(pCursor.getString(6));
            lCity.setmId(pCursor.getInt(0));


        }

        return lCity;
    }

    private Cursor requeteCitiesAll() {
		Cursor cursor = mDb.query(
				TABLE_NAME,
				new String[]{ID_COLUMN_NAME,
                            CITY_COLUMN_NAME,
                            COUNTRY_COLUMN_NAME,
                            VENT_COLUMN_NAME,
                            PRESSION_COLUMN_NAME,
                            TEMPERATURE_COLUMN_NAME,
						    DATE_COLUMN_NAME},
				null, null, null, null, null);
		return cursor;
	}

    public List<City> getAllCities(){
        List<City> lCityList = new ArrayList<City>();
        Cursor lCursor = requeteCitiesAll();
        if (lCursor != null && lCursor.moveToFirst()){
            City cityTmp;
            do{
                cityTmp = cursorToCity(lCursor);
                if ( cityTmp != null){
                    lCityList.add(cityTmp);
                }
            }while(lCursor.moveToNext());
            if(!lCursor.isClosed()){
                lCursor.close();
            }
        }
        return lCityList;
    }

   /* public long addCity(City pCity){
		ContentValues lValues = new ContentValues();


		lValues.put(CITY_COLUMN_NAME, pCity.getmNom());
		lValues.put(COUNTRY_COLUMN_NAME,   pCity.getmPays() );
		lValues.put(VENT_COLUMN_NAME,     pCity.getmDirectionVent());
        lValues.put(TEMPERATURE_COLUMN_NAME,     pCity.getmTemperatureAire());
		lValues.put(DATE_COLUMN_NAME,     pCity.getmDernierReleve());


		return mDb.insert(TABLE_NAME, null, lValues);
	}
    public long deleteCity(City pCity){

        return mDb.delete(TABLE_NAME, ID_COLUMN_NAME +"="+pCity.getmId(),null);
    }


    public long updateCity(City pCity){
        		ContentValues lValues = new ContentValues();


		lValues.put(CITY_COLUMN_NAME, pCity.getmNom());
		lValues.put(COUNTRY_COLUMN_NAME,   pCity.getmPays() );
		lValues.put(VENT_COLUMN_NAME,     pCity.getmDirectionVent());
        lValues.put(TEMPERATURE_COLUMN_NAME,     pCity.getmTemperatureAire());
		lValues.put(DATE_COLUMN_NAME,     pCity.getmDernierReleve());
        lValues.put(PRESSION_COLUMN_NAME, pCity.getmPression());

        return mDb.update(TABLE_NAME,lValues, ID_COLUMN_NAME+"="+pCity.getmId(),null);
    }*/
   public static void addCityIndb(ContentResolver pContentResolver,City pCity){
          ContentValues lValues = Utils.cityValues(pCity);
       /*
        ContentValues lValues = new ContentValues();

       lValues.put(DbCity.CITY_COLUMN_NAME, pCity.getmNom());
       lValues.put(DbCity.COUNTRY_COLUMN_NAME,   pCity.getmPays() );
       lValues.put(DbCity.VENT_COLUMN_NAME,     pCity.getmDirectionVent());
       lValues.put(DbCity.TEMPERATURE_COLUMN_NAME,     pCity.getmTemperatureAire());
       lValues.put(DbCity.DATE_COLUMN_NAME,     pCity.getmDernierReleve());
       */

       pContentResolver.insert(CONTENT_URI, lValues);
   }
    /*private static void updateCityInDb(ContentResolver pContentResolver,City pCity){
        ContentValues lValues = Utils.cityValues(pCity);

        String lUrl = URL+"/"+pCity.getmPays()+"/"+pCity.getmNom();/// id ville & pays unique
        //String lUrl = URL+"/#";
        pContentResolver.update(Uri.parse(lUrl), lValues, null, null);
    }*/

    public static void deleteCityInDb(ContentResolver pContentResolver,City pCity){

        pContentResolver.delete(CONTENT_URI, ID_COLUMN_NAME + "=" + pCity.getmId(), null);
    }
}