package tsv.kalender;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Main extends FragmentActivity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks,
        AdapterView.OnItemSelectedListener,
        MainFragment.Callbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    public static int mCurrentSelectedPosition = 0;

    private boolean mBillingServiceReady;

    InterstitialAd interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);


        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0b63f0")));
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setIcon(new ColorDrawable(android.R.color.transparent));

        AdRequest adRequest = new AdRequest.Builder().addTestDevice("D3273C5162F8E5FEEE9E552AE16A0C0D").build();
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(getString(R.string.ad_interstitial_id));
        interstitial.loadAd(adRequest);

        deleteDatabase("calendar.db");

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        mNavigationDrawerFragment.selectItem(0);

        if (ApplicationContextProvider.getAdresses().size() == 0 || ApplicationContextProvider.getClubs().size() == 0 || ApplicationContextProvider.getContacts().size() == 0 || ApplicationContextProvider.getDates().size() == 0) {
            setLists();
        }

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            MainFragment fragment = new MainFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_container, fragment)
                    .commit();


        }

        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                if (ApplicationContextProvider.showInterstitial()) {
                    displayInterstitial();
                }
            }
        });

    }

    public void setLists() {

        ArrayList<Dates> dates = new ArrayList<Dates>();
        ArrayList<Club> clubs = new ArrayList<Club>();
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        ArrayList<Adress> adresses = new ArrayList<Adress>();

        DateFormat sdf = new SimpleDateFormat("dd.MM.yy", Locale.GERMANY);

        DBHelper database = new DBHelper();

        Cursor cursor = database.select("SELECT * FROM Dates");

        while (cursor.moveToNext()) {

            Dates d = new Dates(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getInt(9), cursor.getInt(10),cursor.getString(5));
            dates.add(d);

        }
        cursor.close();

        cursor = database.select("SELECT * FROM Contacts");

        while (cursor.moveToNext()) {

            Contact c = new Contact(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getInt(7), cursor.getInt(8));
            contacts.add(c);

        }

        cursor.close();

        cursor = database.select("SELECT * FROM Clubs");

        while (cursor.moveToNext()) {
            Club c = new Club(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(5), cursor.getInt(4), cursor.getString(3), cursor.getInt(6));
            clubs.add(c);
        }
        cursor.close();

        cursor = database.select("SELECT * FROM Adresses");

        while (cursor.moveToNext()) {
            Adress a = new Adress(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            adresses.add(a);
        }
        cursor.close();

        ApplicationContextProvider.setDates(dates);
        ApplicationContextProvider.setContacts(contacts);
        ApplicationContextProvider.setClubs(clubs);
        ApplicationContextProvider.setAdresses(adresses);

        cursor.close();
        database.close();

    }


    Spinner spinner;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        spinner = (Spinner) getLayoutInflater().inflate(R.layout.spinner_layout, null);
        String[] sports = getResources().getStringArray(R.array.sports_array);
        CustomSpinnerAdapter s = new CustomSpinnerAdapter(ApplicationContextProvider.getContext(), R.layout.spinnerrow, sports);
        s.setInflater(getLayoutInflater());

        spinner.setAdapter(s);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(ApplicationContextProvider.getIndex());

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(android.app.ActionBar.LayoutParams.MATCH_PARENT, android.app.ActionBar.LayoutParams.MATCH_PARENT);
        getActionBar().setCustomView(spinner, layoutParams);
        getActionBar().setDisplayShowCustomEnabled(true);

        return true;
    }


    int index = 0;

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        Intent i = null;

        switch (position) {
            case 1:
                i = new Intent(ApplicationContextProvider.getContext(), ListingActivity.class);
                i.putExtra("content", "" + index);
                i.putExtra("cal", "" + true);
                break;
            case 2:
                i = new Intent(ApplicationContextProvider.getContext(), ListingActivity.class);
                i.putExtra("content", "" + index);
                i.putExtra("cal", "" + false);
                break;
            case 3:
                i = new Intent(getApplicationContext(), ListingActivity.class);
                i.putExtra("content", "" + index);
                i.putExtra("cal", "" + false);
                i.putExtra("contacts", "");
                break;
            case 4:
                i = new Intent(ApplicationContextProvider.getContext(), Settings.class);

                break;
        }

        if (i != null) {
            startActivity(i);
        }


    }

    public void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        ApplicationContextProvider.setIndex((int) l);
        index = (int) l;

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void callShortcut(int id, int sport) {
        Intent i = new Intent(ApplicationContextProvider.getContext(), ListingActivity.class);
        i.putExtra("content", "" + sport);
        i.putExtra("cal", "" + true);
        i.putExtra("shortcut", id);
        startActivity(i);
    }
}
