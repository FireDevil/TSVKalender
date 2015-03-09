package tsv.kalender;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public class ApplicationContextProvider extends Application {

	/**
	 * Keeps a reference of the application context
	 */
	private static Context sContext;
	private static int index = 0;
    private static boolean twopane = false;
	private static ArrayList<Dates> dates = new ArrayList<Dates>();
	private static ArrayList<Contact> contacts = new ArrayList<Contact>();
	private static ArrayList<Club> clubs = new ArrayList<Club>();
	private static ArrayList<Adress> adresses = new ArrayList<Adress>();
    private static ArrayList<Dates> steadyDates = new ArrayList<Dates>();
    private static ArrayList<Contact> steadyContacts = new ArrayList<Contact>();
    private static ArrayList<Club> steadyClubs = new ArrayList<Club>();
    private static ArrayList<Adress> steadyAdresses = new ArrayList<Adress>();

    private static int interstitial;
    private static int bottom;

    @Override
	public void onCreate() {
		super.onCreate();
		sContext = getApplicationContext();

        DBHelper db = new DBHelper();
        Cursor free = db.select("SELECT count FROM Ads");
        free.moveToFirst();

        interstitial = free.getInt(0);
        free.moveToNext();
        bottom = free.getInt(0);

        free.close();
        db.close();

	}

	/**
	 * Returns the application context
	 *
	 * @return application context
	 */
	public static Context getContext() {
		return sContext;
	}

	public static void setIndex(int i){
		index = i;
	}

	public static int getIndex(){
		return index;
	}

    public static boolean isTwopane() {
        return twopane;
    }

    public static void setTwopane(boolean twopane) {
        ApplicationContextProvider.twopane = twopane;
    }

	public static ArrayList<Dates> getDates() {
		return dates;
	}

	public static ArrayList<Contact> getContacts() {
		return contacts;
	}

	public static ArrayList<Club> getClubs() {
		return clubs;
	}

	public static ArrayList<Adress> getAdresses() {
		return adresses;
	}

	public static void setDates(ArrayList<Dates> dates) {
		ApplicationContextProvider.dates = dates;
	}

	public static void setContacts(ArrayList<Contact> contacts) {
		ApplicationContextProvider.contacts = contacts;
	}

	public static void setClubs(ArrayList<Club> clubs) {
		ApplicationContextProvider.clubs = clubs;
	}

	public static void setAdresses(ArrayList<Adress> adresses) {
		ApplicationContextProvider.adresses = adresses;
	}

    public static boolean showInterstitial() {

        interstitial++;

        DBHelper dbHelper = new DBHelper();
        ContentValues values = new ContentValues();
        values.put("count",interstitial);
        dbHelper.update("Ads",values," name=\"main_interstitial\"");

        dbHelper.close();

        return interstitial%5 == 0;
    }

    public static int getBottom() {
        return bottom;
    }

    public static void setBottom(int bot) {
        bottom = bot;
    }

    public static ArrayList<Dates> getSteadyDates() {
        return steadyDates;
    }

    public static ArrayList<Contact> getSteadyContacts() {
        return steadyContacts;
    }

    public static ArrayList<Club> getSteadyClubs() {
        return steadyClubs;
    }

    public static ArrayList<Adress> getSteadyAdresses() {
        return steadyAdresses;
    }

    public static void setSteadyDates(ArrayList<Dates> dates) {
        ApplicationContextProvider.steadyDates = dates;
    }

    public static void setSteadyContacts(ArrayList<Contact> contacts) {
        ApplicationContextProvider.steadyContacts = contacts;
    }

    public static void setSteadyClubs(ArrayList<Club> clubs) {
        ApplicationContextProvider.steadyClubs = clubs;
    }

    public static void setSteadyAdresses(ArrayList<Adress> adresses) {
        ApplicationContextProvider.steadyAdresses = adresses;
    }
}
