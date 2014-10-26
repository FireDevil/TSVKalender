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
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class PagerActivity extends FragmentActivity implements 
ContentFragment.Callbacks{

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
	int page = 0;
	DBHelper database;
	String where;
	String table;
	ArrayList<Dates> dates;
	ArrayList<Club> clubs;
    ArrayList<Contact> contacts;
	ArrayList<Integer> id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dates_fragment);
		
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0b63f0")));
		getActionBar().setDisplayHomeAsUpEnabled(true);

		content = Integer.parseInt(getIntent().getStringExtra("content"));

		if(getIntent().getStringExtra("cal").equals("true")){
			cal = true;
		}else{
			cal = false;

		}

		
		
		where = "";
		table = "SELECT * FROM ";
		ArrayList<String> arr = new ArrayList<String>();
		id = new ArrayList<Integer>();
		dates = new ArrayList<Dates>();
		clubs = new ArrayList<Club>();
        contacts = new ArrayList<Contact>();

		if(cal){
			table = table + Base.DATES_TABLE;
			getActionBar().setTitle(getResources().getString(R.string.calendar));
		}else{
            if(getIntent().hasExtra("contacts")){
                table = table + Base.CONTACTS_TABLE;
                getActionBar().setTitle(getResources().getString(R.string.contact));
            }else {
                table = table + Base.CLUB_TABLE;
                getActionBar().setTitle(getResources().getString(R.string.title_contacts_list));
            }
		}

		if(content > 0){
			where = " WHERE sportId="+content;
		}

		database = new DBHelper();

		Cursor cursor = database.select(table + where);

		while(cursor.moveToNext()){
			if(cal){
				
				Dates d = new Dates(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getInt(9),cursor.getInt(10),cursor.getString(5));
				dates.add(d);
				arr.add(cursor.getString(3));
			}else{
                if(getIntent().hasExtra("contacts")){
                    Contact c = new Contact(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getInt(7), cursor.getInt(8));
                    contacts.add(c);
                }else {
                    Club c = new Club(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(5), cursor.getInt(4), cursor.getString(3), cursor.getInt(6));
                    clubs.add(c);
                }
                Log.e("",cursor.getString(1));
                arr.add(cursor.getString(1));
			}
			id.add(cursor.getInt(0));
		}

		cursor.close();
		database.close();

		page = Integer.parseInt(getIntent().getStringExtra("id"));

		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		mSectionsPagerAdapter.setCAL(cal);
		mSectionsPagerAdapter.setArr(arr);
		mSectionsPagerAdapter.setContent(content);

        if(getIntent().hasExtra("contacts")) {
            mSectionsPagerAdapter.setContacts(true);
        }else{
            mSectionsPagerAdapter.setContacts(false);
        }

		if(page < 0){
			mSectionsPagerAdapter.setId(""+0);
		}
		mSectionsPagerAdapter.setId(""+page);

		mViewPager = (ViewPager) findViewById(R.id.twoPager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setCurrentItem(page);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.pager, menu);
		
		if(!cal){
			menu.findItem(R.id.add_dates).setVisible(false);
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_dates:
			
			int id = 0;

			database = new DBHelper();

			Cursor c = database.select("SELECT * FROM "+Base.CALENDAR_TABLE+" WHERE selected = 'true'");

			if(c.getCount()== 0){
				c.close();
				database.close();

				Intent intent = new Intent(ApplicationContextProvider.getContext(),Settings.class);
				startActivity(intent);
				return false;
			}else{
				c.moveToFirst();
				id = c.getInt(1);
			}

			c.close();

			Cursor cursor = database.select("SELECT * FROM "+Base.DATES_TABLE+" WHERE _id="+this.id.get(mViewPager.getCurrentItem()));
			cursor.moveToFirst();

			if(cursor.getString(3).length() == 0){
				cursor.close();
				database.close();
				return false;
			}
			
			int size = cursor.getString(3).length();

			Calendar beginTime = Calendar.getInstance();
			beginTime.set(Integer.parseInt(cursor.getString(3).substring(size-4,size)), Integer.parseInt(cursor.getString(3).substring(size-7, size-5))-1, Integer.parseInt(cursor.getString(3).substring(size-10,size-8)));

			size = cursor.getString(4).length();
			Calendar endTime = Calendar.getInstance();
			endTime.set(Integer.parseInt(cursor.getString(4).substring(size-4,size )), Integer.parseInt(cursor.getString(4).substring(size-7, size-5))-1, Integer.parseInt(cursor.getString(4).substring(size-10,size-8)));

			ContentValues values = new ContentValues();

			if(beginTime.equals(endTime)){
				values.put(Events.ALL_DAY, true);
				values.put(Events.DTSTART,beginTime.getTimeInMillis());
				values.put(Events.DURATION, "+P2H");
			}else{
				values.put(Events.ALL_DAY, true);
				values.put(Events.DTSTART, beginTime.getTimeInMillis());
				values.put(Events.DTEND, endTime.getTimeInMillis());	
			}

			values.put(Events.TITLE, cursor.getString(1));
			values.put(Events.CALENDAR_ID,id);
			values.put(Events.EVENT_LOCATION, cursor.getString(2));
			values.put(Events.EVENT_COLOR, Color.parseColor("#0b63f0"));
			values.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().toString());
			Uri uri = ApplicationContextProvider.getContext().getContentResolver().insert(Events.CONTENT_URI, values);
			long eventID = Long.parseLong(uri.getLastPathSegment());

			ContentValues reminders = new ContentValues();
			reminders.put(Reminders.EVENT_ID, eventID);
			reminders.put(Reminders.METHOD, Reminders.METHOD_ALERT);
			reminders.put(Reminders.MINUTES,4320);

			Uri uri2 = ApplicationContextProvider.getContext().getContentResolver().insert(Reminders.CONTENT_URI, reminders);

			cursor.close();
			database.close();

			LayoutInflater inflate = getLayoutInflater();
			View v = inflate.inflate(R.layout.check, (ViewGroup)findViewById(R.id.toast_ll));
			
			Toast t = new Toast(getApplicationContext());
			t.setDuration(Toast.LENGTH_SHORT);
			t.setGravity(Gravity.TOP, 0, 0);
			t.setView(v);
			t.show();
			
			return true;
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public ArrayList<Dates> getDates() {
		return this.dates;
	}

	@Override
	public ArrayList<Club> getClubs() {
		return this.clubs;
	}

    @Override
    public ArrayList<Contact> getContacts() {
        return this.contacts;
    }
}
