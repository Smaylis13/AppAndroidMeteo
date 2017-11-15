package m2rism.myappmeteo.classes;

import java.io.Serializable;

/**
 * Created by lyamsi on 24/09/15.
 */
public class City implements Serializable{
    private int mId;
    private String mNom;
    private String mPays;
    private String mDernierReleve;
    private String mDirectionVent;
    private String mPression;// en hPa
    private String mTemperatureAire; // en degré Celsius

    public void setmId(int mId) {
        this.mId = mId;
    }

    public int getmId() {
        return mId;
    }

    public City() {
    }
    public City(String lNom, String lPays){
        mNom = lNom;
        mPays = lPays;
        mDernierReleve="";
        mDirectionVent="";
        mPression="";
        mTemperatureAire="";
    }

    public String getmNom() {
        return mNom;
    }

    public String getmPays() {
        return mPays;
    }

    public void setmPression(String mPression) {
        this.mPression = mPression;
    }

    public void setmDirectionVent(String mDirectionVent) {
        this.mDirectionVent = mDirectionVent;
    }

    public void setmTemperatureAire(String mTemperatureAire) {
        this.mTemperatureAire = mTemperatureAire;
    }

    public void setmDernierReleve(String mDernierReleve) {
        this.mDernierReleve = mDernierReleve;
    }

    @Override
    public String toString() {
        return "City{" +
                "mNom='" + mNom + '\'' +
                ", mPays='" + mPays + '\'' +
                ", mDernierReleve='" + mDernierReleve + '\'' +
                ", mDirectionVent='" + mDirectionVent + '\'' +
                ", mPression='" + mPression + '\'' +
                ", mTemperatureAire='" + mTemperatureAire + '\'' +
                '}';
    }

    public String getmDernierReleve() {
        return mDernierReleve;
    }

    public String getmDirectionVent() {
        return mDirectionVent;
    }

    public String getmPression() {
        return mPression;
    }

    public String getmTemperatureAire() {
        return mTemperatureAire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        if (mNom != null ? !mNom.equals(city.mNom) : city.mNom != null) return false;
        return !(mPays != null ? !mPays.equals(city.mPays) : city.mPays != null);

    }

    @Override
    public int hashCode() {
        int result = mNom != null ? mNom.hashCode() : 0;
        result = 31 * result + (mPays != null ? mPays.hashCode() : 0);
        return result;
    }
}
