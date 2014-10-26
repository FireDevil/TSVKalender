package tsv.kalender;

import android.app.Activity;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class Settings extends Activity{

	ArrayList<String> calendars = new ArrayList<String>();

	DBHelper database;

	String bool;
	int sel;

	int y;
	int m;
	int d;

	Spinner sp;
	ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Holo);
		setContentView(R.layout.settings);

		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0b63f0")));
        getActionBar().setDisplayHomeAsUpEnabled(true);

		database = new DBHelper();

		getCalendars();
		Cursor cursor = database.select("SELECT * FROM "+Base.CALENDAR_TABLE);

		while(cursor.moveToNext()){
            calendars.add(cursor.getString(2));

			if(cursor.getString(3) == null){

			}else{
				if(cursor.getString(3).equals("true")){
					sel = cursor.getInt(0);
					if(sel > 0){
						sel--;
					}
				}
			}
		}

		database.close();
		cursor.close();

		ArrayAdapter<String> arr = new ArrayAdapter<String>(getApplicationContext(),R.layout.settings_row,R.id.itemtext,calendars);

		sp = (Spinner)findViewById(R.id.settingsspinner);
		sp.setAdapter(arr);
		sp.setSelection(sel);
		sp.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				database = new DBHelper();

				Cursor c = database.select("SELECT * FROM "+Base.CALENDAR_TABLE);

				if(c.getCount()== 0){
					return;
				}

				while(c.moveToNext()){

					if(c.getInt(0) == ((int)arg3+1)){
						ContentValues values = new ContentValues();
						values.put("selected", "true");

						database.update(Base.CALENDAR_TABLE, values, " _id="+c.getInt(0));
					}else{
						ContentValues values = new ContentValues();
						values.put("selected", "");
						database.update(Base.CALENDAR_TABLE, values, " _id="+c.getInt(0));
					}
				}

				c.close();
				database.close();
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	private void getCalendars() {
		
		Cursor c = database.select("SELECT * FROM "+Base.CALENDAR_TABLE);
		
		String[] l_projection = new String[]{"_id", "name"};
		Uri l_calendars;
		if (Build.VERSION.SDK_INT >= 8 ) {
			l_calendars = Uri.parse("content://com.android.calendar/calendars");
		} else {
			l_calendars = Uri.parse("content://calendar/calendars");
		}
		CursorLoader loader = new CursorLoader(getApplicationContext(), l_calendars, l_projection, null, null, null);
		Cursor l_managedCursor = this.managedQuery(l_calendars, l_projection, null, null, null);    //all calendars
		//	    Cursor l_managedCursor = this.managedQuery(l_calendars, l_projection, "selected=1", null, null);   //active calendars

		while(l_managedCursor.moveToNext()){
			ContentValues values = new ContentValues();
			values.put("calId", l_managedCursor.getInt(0));
			values.put("name", l_managedCursor.getString(1));
			
			if((l_managedCursor.getString(1)+"").equals("null")){
				continue;
			}
			
			if(c.moveToNext()){
				if(l_managedCursor.getInt(0)==c.getInt(1) && l_managedCursor.getString(1).equals(c.getString(2))){

				}else{
					database.insert(Base.CALENDAR_TABLE, values);
				}
			}
			
			if(c.getCount()== 0){
				database.insert(Base.CALENDAR_TABLE, values);
			}
		}
		c.close();
		
//		while(l_managedCursor.moveToNext()){
//			
//			
//			if(l_managedCursor.getString(1).equals("null") || l_managedCursor.getString(1).equals(null) || l_managedCursor.getString(1).equals("")){
//				continue;
//			}
//			
//			ContentValues values = new ContentValues();
//			values.put("calId", l_managedCursor.getInt(0));
//			values.put("name", l_managedCursor.getString(1));
//
//			if(c.moveToNext()){
//				if(l_managedCursor.getInt(0)==c.getInt(1) && l_managedCursor.getString(1).equals(c.getString(2))){
//
//				}else{
//					database.insert(Base.CALENDAR_TABLE, values, db);
//				}
//			}
//			
//			if(c.getCount()== 0){
//				database.insert(Base.CALENDAR_TABLE, values, db);
//			}
//		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.check:
			database = new DBHelper();

			Cursor c = database.select("SELECT * FROM "+Base.CALENDAR_TABLE);
			if(c.getCount()==0){
				Toast.makeText(getApplicationContext(), getResources().getText(R.string.no_calendar), Toast.LENGTH_SHORT).show();
				finish();
				return false;
			}

			while(c.moveToNext()){
				if(c.getInt(0) == ((int)sp.getSelectedItemPosition()+1)){
					ContentValues values = new ContentValues();
					values.put("selected", "true");

					database.update(Base.CALENDAR_TABLE, values, " _id="+c.getInt(0));
				}else{
					ContentValues values = new ContentValues();
					values.put("selected", "");
					database.update(Base.CALENDAR_TABLE, values, " _id="+c.getInt(0));
				}

			}

			c.close();
			database.close();
			finish();

			return true;
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
