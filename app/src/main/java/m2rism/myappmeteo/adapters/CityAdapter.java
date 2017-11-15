package m2rism.myappmeteo.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import m2rism.myappmeteo.R;
import m2rism.myappmeteo.classes.City;

/**
 * Created by lyamsi on 01/01/01.
 */
public class CityAdapter extends BaseAdapter {
    //le contexte dans lequel est présent notre adapter
    private Context mContext;
    //Un mécanisme pour gérer l'affichage graphique depuis un layout XML
    private LayoutInflater mInflater;
    //A list of Choices
    private List<City> mListCity;

    public CityAdapter(Context context, List<City> listCity){
        this.mContext=context;
        this.mListCity=listCity;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public List<City> getmListCandidat() {
        return mListCity;
    }


    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return mListCity.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return  mListCity.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if( convertView == null ){
                //We must create a View:
                convertView = mInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }
            //Here we can do changes to the convertView, such as set a text on a TextView
            //or an image on an ImageView.
        TextView lName = (TextView)convertView.findViewById(android.R.id.text1);

        lName.setText(mListCity.get(position).getmNom()+" ("+mListCity.get(position).getmPays()+")");
        lName.setTextColor(Color.BLACK);

        return convertView;
    }
}
