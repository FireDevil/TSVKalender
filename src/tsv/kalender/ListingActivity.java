package tsv.kalender;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Vector;

/**
 * An activity representing a list of Dates. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link ContentDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ListingFragment} and the item details (if present) is a
 * {@link ContentFragment}.
 * <p/>
 * This activity also implements the required
 * {@link ListingFragment.Callbacks} interface to listen for item
 * selections.
 */
public class ListingActivity extends FragmentActivity implements
        ListingFragment.Callbacks,
        DatesAdapter.Callbacks,
        CalendarFragment.Callbacks,
        ContentFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    int content;
    boolean cal;
    boolean all = false;

    int page = -1;
    int shortcut = 0;
    static Menu mMenu;
    String date;

    DBHelper database;

    public Vector<Integer> checked = new Vector<Integer>();
    public ArrayList<Dates> dates;
    public ArrayList<Club> clubs;
    public ArrayList<Contact> contacts;
    ArrayList<String> arr;

    AdView adView;
    InterstitialAd interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_list);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice("D3273C5162F8E5FEEE9E552AE16A0C0D").build();
        adView = (AdView) findViewById(R.id.adView);
        adView.loadAd(adRequest);
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(getString(R.string.ad_interstitial_id));
        interstitial.loadAd(adRequest);
        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                if(ApplicationContextProvider.showInterstitial()){
                    interstitial.show();
                }
            }
        });


        if (getIntent().getStringExtra("content") == null) {
            content = ApplicationContextProvider.getIndex();
        } else {
            content = Integer.parseInt(getIntent().getStringExtra("content"));
        }


        cal = getIntent().getStringExtra("cal").equals("true");


        if (savedInstanceState == null) {
            ListingFragment fragment = new ListingFragment();
            Bundle args = new Bundle();
            if (getIntent().hasExtra("contacts")) {
                args.putBoolean("contacts", true);
            }
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.calendar_list, fragment).commit();
        }

        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (!cal) {
            getActionBar().setTitle(getResources().getString(R.string.title_contacts_list));
        } else {
            getActionBar().setTitle(getResources().getString(R.string.calendar_dates));
        }

        if (getIntent().hasExtra("contacts")) {
            getActionBar().setTitle(getResources().getString(R.string.contact));
        }

        database = new DBHelper();

        arr = new ArrayList<String>();
        dates = new ArrayList<Dates>();
        clubs = new ArrayList<Club>();
        contacts = new ArrayList<Contact>();

        String where = "";
        String table = "SELECT * FROM ";

        if (cal) {
            table = table + Base.DATES_TABLE;
        } else {

            if (getIntent().hasExtra("contacts")) {
                table = table + Base.CONTACTS_TABLE;
            } else {
                table = table + Base.CLUB_TABLE;
            }

        }


        if (content > 0) {
            where = " WHERE sportId=" + content;
        }

        database = new DBHelper();

        Cursor cursor = database.select(table + where);

        while (cursor.moveToNext()) {
            if (cal) {

                Dates d = new Dates(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getInt(9), cursor.getInt(10), cursor.getString(5));
                dates.add(d);
                arr.add(cursor.getString(3));
                if (getIntent().hasExtra("shortcut")) {
                    if (getIntent().getIntExtra("shortcut", 0) == d.get_id()) {
                        onItemSelected("" + (arr.size() - 1));
                    }
                }
            } else {

                if (getIntent().hasExtra("contacts")) {
                    Contact c = new Contact(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getInt(7), cursor.getInt(8));
                    contacts.add(c);
                } else {
                    Club c = new Club(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(5), cursor.getInt(4), cursor.getString(3), cursor.getInt(6));
                    clubs.add(c);
                }
                arr.add(cursor.getString(1));
            }
        }

        cursor.close();
        database.close();


        if (findViewById(R.id.twoPager) != null) {

            getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0b63f0")));
            mTwoPane = true;
            mSectionsPagerAdapter = new SectionsPagerAdapter(
                    getSupportFragmentManager());
            mSectionsPagerAdapter.setCAL(cal);
            mSectionsPagerAdapter.setArr(arr);
            mSectionsPagerAdapter.setContent(content);

            if (getIntent().hasExtra("contacts")) {
                mSectionsPagerAdapter.setContacts(true);
            } else {
                mSectionsPagerAdapter.setContacts(false);
            }

            if (page < 0) {
                mSectionsPagerAdapter.setId("" + 0);
            }
            mSectionsPagerAdapter.setId("" + page);

            mViewPager = (ViewPager) findViewById(R.id.twoPager);
            mViewPager.setAdapter(mSectionsPagerAdapter);
            mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

                @Override
                public void onPageScrollStateChanged(int arg0) {

                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                    date = arr.get(arg0);
                }

                @Override
                public void onPageSelected(int arg0) {

                    if (findViewById(R.id.calendarView1) == null) {

                        ListView lv = ((ListingFragment) getSupportFragmentManager().findFragmentById(R.id.calendar_list)).getListView();

                        if (lv.getCount() < arg0) {
                            arg0 = 0;
                        }

                        page = arg0;
                        lv.setSelection(arg0);

                    } else {

                        if (!arr.contains(date)) {

                        } else {
                            String d = arr.get(arg0);
                            int size = d.length();

                            Calendar date = Calendar.getInstance();
                            date.set(Integer.parseInt(d.substring(size - 4, size)), Integer.parseInt(d.substring(size - 7, size - 5)) - 1, Integer.parseInt(d.substring(size - 10, size - 8)));

                            CalendarView cv = (CalendarView) getSupportFragmentManager().findFragmentById(R.id.calendar_list).getView().findViewById(R.id.calendarView1);
                            cv.setDate(date.getTimeInMillis());
                        }
                    }
                }
            });
        }

    }

    // Invoke displayInterstitial() when you are ready to display an interstitial.
    public void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list, menu);
        mMenu = menu;

        if (arr.size() == 0) {
            MenuItem i = (MenuItem) menu.findItem(R.id.select_all);
            i.setVisible(false);
            i = (MenuItem) menu.findItem(R.id.calendarSign);
            i.setVisible(false);
            i = (MenuItem) menu.findItem(R.id.list_insert);
            i.setVisible(false);
        }

        if (!cal) {
            MenuItem add = (MenuItem) menu.findItem(R.id.select_all);
            add.setVisible(false);
            add = (MenuItem) menu.findItem(R.id.calendarSign);
            add.setVisible(false);
        }

        if (getIntent().hasExtra("contacts")) {
            MenuItem i = (MenuItem) menu.findItem(R.id.select_all);
            i.setVisible(false);
            i = (MenuItem) menu.findItem(R.id.calendarSign);
            i.setVisible(false);
            i = (MenuItem) menu.findItem(R.id.list_insert);
            i.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.list_insert:

                insertItems(((ListingFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.calendar_list))
                        .getListView());

                clearLV(((ListingFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.calendar_list))
                        .getListView());
                return true;
            case R.id.select_all:

                if (all) {
                    clearLV(((ListingFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.calendar_list))
                            .getListView());
                    item.setTitle(getResources().getString(R.string.select));
                    item.setIcon(R.drawable.select);
                    all = false;
                } else {
                    selectLV(((ListingFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.calendar_list))
                            .getListView());
                    item.setTitle(getResources().getString(R.string.deselect));
                    item.setIcon(R.drawable.deselect);
                    all = true;
                }

                return true;
            case R.id.calendarSign:

                if (findViewById(R.id.calendarView1) == null) {
                    displayInterstitial();
                    getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0b63f0")));
                    CalendarFragment fragment = new CalendarFragment();
                    ListingFragment f1 = ((ListingFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.calendar_list));
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.remove(f1);
                    transaction.replace(R.id.calendar_list, fragment);
                    transaction.commit();
                    MenuItem add = (MenuItem) mMenu.findItem(R.id.select_all);
                    add.setVisible(false);
                    add = (MenuItem) mMenu.findItem(R.id.list_insert);
                    add.setVisible(false);
                    item.setTitle(getResources().getString(R.string.list));
                    item.setIcon(R.drawable.list);

                    return true;
                } else {

                    if (findViewById(R.id.twoPager) == null) {
                        getActionBar().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
                    } else {
                        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0b63f0")));
                    }
                    CalendarFragment cal = new CalendarFragment();
                    ListingFragment fragment = new ListingFragment();
                    Bundle args = new Bundle();
                    if(getIntent().hasExtra("contacts")){
                        args.putBoolean("contacts",true);
                    }
                    fragment.setArguments(args);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.calendar_list, fragment);
                    transaction.remove(cal);
                    transaction.commit();
                    MenuItem add = (MenuItem) mMenu.findItem(R.id.select_all);
                    add.setVisible(true);
                    add = (MenuItem) mMenu.findItem(R.id.list_insert);
                    add.setVisible(true);
                    item.setTitle(getResources().getString(R.string.calendar));
                    item.setIcon(R.drawable.cal);
                }

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Callback method from {@link ListingFragment.Callbacks} indicating
     * that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            int page = Integer.parseInt(id);
            ((ListingFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.calendar_list))
                    .getListView().setSelection(Integer.parseInt(id));
            mViewPager.setCurrentItem(page);

        } else {
            Intent detailIntent = new Intent(this, PagerActivity.class);
            detailIntent.putExtra("id", id);
            detailIntent.putExtra("content", "" + content);
            detailIntent.putExtra("cal", "" + cal);

            if (getIntent().hasExtra("contacts")) {
                detailIntent.putExtra("contacts", "");
            }

            startActivity(detailIntent);
        }
    }

    @Override
    public boolean getCal() {

        return cal;
    }

    public void insertItems(ListView lv) {
        for (int i = 1; i <= lv.getCount(); i++) {
            if (checked.contains(i)) {


                int id = 0;

                database = new DBHelper();

                Cursor c = database.select("SELECT * FROM " + Base.CALENDAR_TABLE + " WHERE selected= 'true'");

                if (c.getCount() == 0) {
                    c.close();
                    database.close();

                    Intent intent = new Intent(ApplicationContextProvider.getContext(), Settings.class);
                    startActivity(intent);
                    return;
                } else {
                    c.moveToFirst();
                    id = c.getInt(1);
                }

                c.close();

                if (dates.get(i).getStartDate().length() == 0) {
                    database.close();
                    continue;
                }


                int size = dates.get(i).getStartDate().length();

                Calendar beginTime = Calendar.getInstance();
                beginTime.set(Integer.parseInt(dates.get(i).getStartDate().substring(size - 4, size)), Integer.parseInt(dates.get(i).getStartDate().substring(size - 7, size - 5)) - 1, Integer.parseInt(dates.get(i).getStartDate().substring(size - 10, size - 8)));

                size = dates.get(i).getEndDate().length();
                Calendar endTime = Calendar.getInstance();
                endTime.set(Integer.parseInt(dates.get(i).getEndDate().substring(size - 4, size)), Integer.parseInt(dates.get(i).getEndDate().substring(size - 7, size - 5)) - 1, Integer.parseInt(dates.get(i).getEndDate().substring(size - 10, size - 8)));

                ContentValues values = new ContentValues();

                if (beginTime.equals(endTime)) {
                    values.put(Events.ALL_DAY, true);
                    values.put(Events.DTSTART, beginTime.getTimeInMillis());
                    values.put(Events.DURATION, "+P2H");
                } else {
                    values.put(Events.ALL_DAY, true);
                    values.put(Events.DTSTART, beginTime.getTimeInMillis());
                    values.put(Events.DTEND, endTime.getTimeInMillis());
                }

                values.put(Events.TITLE, dates.get(i).getDescription());
                values.put(Events.CALENDAR_ID, id);
                values.put(Events.EVENT_LOCATION, dates.get(i).getLocation());
                values.put(Events.EVENT_COLOR, Color.parseColor("#0b63f0"));
                values.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().toString());
                Uri uri = ApplicationContextProvider.getContext().getContentResolver().insert(Events.CONTENT_URI, values);
                long eventID = Long.parseLong(uri.getLastPathSegment());

                ContentValues reminders = new ContentValues();
                reminders.put(Reminders.EVENT_ID, eventID);
                reminders.put(Reminders.METHOD, Reminders.METHOD_ALERT);
                reminders.put(Reminders.MINUTES, 2000);

                ApplicationContextProvider.getContext().getContentResolver().insert(Reminders.CONTENT_URI, reminders);

                database.close();

                LayoutInflater inflate = getLayoutInflater();
                View v = inflate.inflate(R.layout.check, (ViewGroup) findViewById(R.id.toast_ll));

                Toast t = new Toast(getApplicationContext());
                t.setDuration(Toast.LENGTH_SHORT);
                t.setGravity(Gravity.TOP, 0, 0);
                t.setView(v);
                t.show();


            }
        }
    }

    public void selectLV(ListView lv) {
        checked.clear();
        for (int i = 1; i < lv.getCount(); i++) {

            if (((Dates) lv.getItemAtPosition(i)).isChecked()) {

            } else {
                ((Dates) lv.getItemAtPosition(i)).setChecked(true);
            }

            onItemChecked(i);
        }
        DatesAdapter da = ((ListingFragment) getSupportFragmentManager().findFragmentById(R.id.calendar_list)).getAdapter();
        da.notifyDataSetChanged();

    }

    public void clearLV(ListView lv) {


        for (int i = 1; i < lv.getCount(); i++) {

            if (!checked.contains(i)) {
                checked.add(i);
            }

            if (((Dates) lv.getItemAtPosition(i)).isChecked()) {
                ((Dates) lv.getItemAtPosition(i)).setChecked(false);
            }

            onItemChecked(i);
        }

        DatesAdapter da = ((ListingFragment) getSupportFragmentManager().findFragmentById(R.id.calendar_list)).getAdapter();
        da.notifyDataSetChanged();
    }

    @Override
    public void onItemChecked(int id) {
        int i = 0;
        ListView lv = ((ListingFragment) getSupportFragmentManager()
                .findFragmentById(R.id.calendar_list))
                .getListView();


        if (checked.contains(id)) {
            checked.remove(checked.indexOf(id));
            ((Dates) lv.getItemAtPosition(id)).setChecked(false);
        } else {
            checked.add(id);
            ((Dates) lv.getItemAtPosition(id)).setChecked(true);
        }

        i = checked.size();

        if (i == lv.getCount()) {
            mMenu.findItem(R.id.select_all).setTitle(getResources().getString(R.string.deselect));
            mMenu.findItem(R.id.select_all).setIcon(R.drawable.deselect);
            all = true;
        }

        if (i > 0) {

            mMenu.findItem(R.id.list_insert).setVisible(true);
            mMenu.findItem(R.id.list_insert).setTitle(checked.size() + " " + getResources().getString(R.string.selected));
        } else {
            mMenu.findItem(R.id.list_insert).setVisible(false);
            mMenu.findItem(R.id.select_all).setIcon(R.drawable.select);
            mMenu.findItem(R.id.select_all).setTitle(getResources().getString(R.string.select));
            all = false;
        }

    }


    @Override
    public void setDateList(ArrayList<Dates> d) {
        this.dates = d;

    }

    @Override
    public void onDateSelected(String id) {

        date = id;
        int page = 0;
        int y;
        int m;
        int d;

        int iY = Integer.parseInt(id.substring(id.length() - 4, id.length()));
        int iM = Integer.parseInt(id.substring(id.length() - 7, id.length() - 5));
        int iD = Integer.parseInt(id.substring(id.length() - 10, id.length() - 8));

        for (String s : arr) {
            if (s.equals("")) {
                continue;
            }
            y = Integer.parseInt(s.substring(s.length() - 4, s.length()));
            m = Integer.parseInt(s.substring(s.length() - 7, s.length() - 5));
            d = Integer.parseInt(s.substring(s.length() - 10, s.length() - 8));

            if (y < iY) {
                if (arr.indexOf(s) == arr.size() - 1) {
                    page = arr.size() - 1;
                }
                continue;
            }

            if (y > iY) {
                page = 0;
                break;
            }

            if (m < iM) {
                if (arr.indexOf(s) == arr.size() - 1) {
                    page = arr.size() - 1;
                }
                continue;
            }

            if (m > iM) {
                page = 0;
                break;
            }

            if (d < iD) {
                if (arr.indexOf(s) == arr.size() - 1) {
                    page = arr.size() - 1;
                }
                continue;
            } else {
                page = arr.indexOf(s);
                break;
            }

        }


        if (mTwoPane) {

            mViewPager.setCurrentItem(page);

        } else {

            Intent detailIntent = new Intent(this, PagerActivity.class);
            detailIntent.putExtra("date", "" + page);
            detailIntent.putExtra("id", "" + page);
            detailIntent.putExtra("content", "" + content);
            detailIntent.putExtra("cal", "" + cal);

            startActivity(detailIntent);
        }

    }

    public static Menu getMenu() {
        return mMenu;
    }

    @Override
    public ArrayList<Dates> getDates() {
        return this.dates;
    }

    @Override
    public ArrayList<Dates> getDates(String where) {
        database = new DBHelper();

        Cursor cursor = database.select(where);
        dates.clear();
        arr.clear();

        while (cursor.moveToNext()) {
            Dates d = new Dates(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getInt(9), cursor.getInt(10),cursor.getString(5));
            dates.add(d);
            arr.add(cursor.getString(3));
        }

        if (mTwoPane) {
            mSectionsPagerAdapter.setArr(arr);
            mViewPager.setAdapter(mSectionsPagerAdapter);
        }

        cursor.close();
        database.close();
        return dates;
    }

    @Override
    public ArrayList<Club> getClubs() {
        return this.clubs;
    }

    @Override
    public ArrayList<Club> getClubs(String where) {
        database = new DBHelper();

        clubs.clear();
        arr.clear();
        Cursor cursor = database.select(where);

        while (cursor.moveToNext()) {
            Club c = new Club(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(5), cursor.getInt(4), cursor.getString(3), cursor.getInt(6));
            clubs.add(c);
            arr.add(cursor.getString(1));
        }

        if (mTwoPane) {
            mSectionsPagerAdapter.setArr(arr);
            mViewPager.setAdapter(mSectionsPagerAdapter);
        }

        cursor.close();
        database.close();
        return clubs;
    }

    @Override
    public ArrayList<Contact> getContacts(){
        return this.contacts;
    }

    @Override
    public  ArrayList<Contact> getContacts(String where){
        database = new DBHelper();

        contacts.clear();
        arr.clear();

        Cursor cursor = database.select(where);

        while (cursor.moveToNext()) {
            Contact c = new Contact(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getInt(7), cursor.getInt(8));
            contacts.add(c);
            arr.add(cursor.getString(1));
        }

        if (mTwoPane) {
            mSectionsPagerAdapter.setArr(arr);
            mViewPager.setAdapter(mSectionsPagerAdapter);
        }

        cursor.close();
        database.close();
        return contacts;
    }

    @Override
    public void onPause() {
        adView.pause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        adView.resume();
    }

    @Override
    public void onDestroy() {
        adView.destroy();
        super.onDestroy();
    }

}
