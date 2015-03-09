package tsv.kalender;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import java.util.ArrayList;

/**
 * An activity representing a single Calendar detail screen. This activity is
 * only used on handset devices. On tablet-size devices, item details are
 * presented side-by-side with a list of items in a {@link ListingActivity}
 * .
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link ContentFragment}.
 */
public class ContentDetailActivity extends FragmentActivity implements
ContentFragment.Callbacks{

	int content;
	boolean cal;
	int id;
	
	static ArrayList<Club> clubs = new ArrayList<Club>();
	static ArrayList<Dates> dates = new ArrayList<Dates>();
    static ArrayList<Contact> contacts = new ArrayList<Contact>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content);

		
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0b63f0")));
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("Details");
		
		content = ApplicationContextProvider.getIndex();
		
		if(getIntent().getStringExtra("cal").equals("true")){
			cal = true;
		}else{
			cal = false;

		}
		
		id = Integer.parseInt(getIntent().getStringExtra("id"));
		

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Fragment fragment = new ContentFragment();
			Bundle args = new Bundle();
			args.putInt("content",content);
			
			
			if(getIntent().hasExtra("club")){
				args.putInt("club", getIntent().getIntExtra("club",0));
			}
			
			if(getIntent().hasExtra("con")){
				args.putString("con","true");
			}

            if(getIntent().hasExtra("steady")){
                args.putString("steady","");
            }
			
			args.putString("id", ""+id);
			args.putString("cal", ""+cal);
			fragment.setArguments(args);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.content_container, fragment).commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static void setArrays(ArrayList<Dates> d, ArrayList<Club> c,ArrayList<Contact> con) {
		dates = d;
		clubs = c;
        contacts = con;
	}

	@Override
	public ArrayList<Dates> getDates() {
		return dates;
	}

	@Override
	public ArrayList<Club> getClubs() {
		return clubs;
	}

    @Override
    public ArrayList<Contact> getContacts() {
        return contacts;
    }
}
