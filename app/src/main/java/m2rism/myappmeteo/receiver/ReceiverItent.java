package m2rism.myappmeteo.receiver;

import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import m2rism.myappmeteo.MainActivity;
import m2rism.myappmeteo.classes.City;
import m2rism.myappmeteo.db.DbCity;
import m2rism.myappmeteo.global.Utils;

/**
 * Created by lyamsi on 16/10/15.
 */
public class ReceiverItent extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(Utils.ACTION_UPDATE)){

			int index = intent.getIntExtra(Utils.POSITION,0);
			City city = (City) intent.getSerializableExtra(Utils.EXTRA_KEY_UPDATE);
			Log.i("TAG","J'ai reçu " + city);
			MainActivity.sArrayCity.set(index, city);


			// UPADTE CITY WITH NEW VALUES
			context.getContentResolver().update(Utils.cityUri(city),Utils.cityValues(city),null,null);
			//DbCity lDb = new DbCity(context);
			//lDb.open();
			//get
			//lDb.updateCity(city);

			//lDb.close();


		}

	}
}

