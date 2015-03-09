package tsv.kalender;

import android.app.SearchManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;



public class SearchActivity extends FragmentActivity {

    private SearchFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0b63f0")));

        String query="";
        if (getIntent().hasExtra(SearchManager.QUERY)) {
            query = getIntent().getStringExtra(SearchManager.QUERY);
        }

        getActionBar().setTitle("Suchergebnisse für '"+query+"'");

        searchFragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(SearchManager.QUERY,query);
        searchFragment.setArguments(args);

        getFragmentManager().beginTransaction()
                .replace(R.id.searchview_container, searchFragment)
                .commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
            return  true;
        }
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return false;
    }
}
