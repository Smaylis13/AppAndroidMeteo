package m2rism.myappmeteo;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.UriMatcher;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by lyamsi on 22/10/15.
 */
public class Test {

    public static void main(String[] args) {
        /*ArrayList<String> list=new ArrayList<String>();

        String[] locales = Locale.getISOCountries();

        for (String countryCode : locales) {

            Locale obj = new Locale("", countryCode);

            System.out.println("Country Name = " + obj.getDisplayCountry());
            list.add(obj.getDisplayCountry());
            */
        System.out.println(ContentResolver.CURSOR_DIR_BASE_TYPE);
    }
}
