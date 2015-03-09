package tsv.kalender;


import android.app.Fragment;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private ArrayList<SearchResultItem> resultItems;
    private SearchResultAdapter adapter;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        String query = getArguments().getString(SearchManager.QUERY);

        DBHelper dbHelper = new DBHelper();
        Cursor datesCursor = dbHelper.select("SELECT rowId,description,startDate FROM Dates WHERE description LIKE '%"+query+"%'");
        Cursor clubCursor = dbHelper.select("SELECT rowId,name, internet FROM Clubs WHERE name LIKE '%"+query+"%'");
        Cursor contactCursor = dbHelper.select("SELECT rowId,name, mail FROM Contacts WHERE name LIKE '%"+query+"%'");
        updateList(datesCursor,clubCursor,contactCursor);

        adapter = new SearchResultAdapter(getActivity(),resultItems);
        ListView lv = (ListView)rootView.findViewById(R.id.search_result_list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SearchResultItem item = resultItems.get(position);
                int content = ApplicationContextProvider.getIndex();

                Intent intent = new Intent(ApplicationContextProvider.getContext(), ContentDetailActivity.class);
                intent.putExtra("id", "" + item.getId());
                intent.putExtra("steady","");

                if(item.getTable().equals("Dates")){

//                    TODO Dates ID startet bei 1
                    if(item.getId() >= 10) {
                        intent.putExtra("id", "" + (item.getId()-1));
                    }

                    intent.putExtra("cal", "true");
                    intent.putExtra("content", content);
                }

                if (item.getTable().equals("Clubs")){
                    intent.putExtra("cal", "false");
                    intent.putExtra("content", content);
                    intent.putExtra("club", item.getId());
                }

                if(item.getTable().equals("Contacts")){
                    intent.putExtra("cal", "false");
                    intent.putExtra("content", content);
                    intent.putExtra("con", "true");
                }

                startActivity(intent);
            }
        });

        return rootView;
    }


    public void updateList(Cursor dates, Cursor clubs, Cursor contacts) {

        resultItems = new ArrayList<SearchResultItem>();

        while (dates.moveToNext()){
            SearchResultItem item = new SearchResultItem();
            item.setId(dates.getInt(0));
            item.setDescription(dates.getString(1));
            item.setTable("Dates");
            item.setExtra(dates.getString(2));
            resultItems.add(item);
        }

        dates.close();

        while (clubs.moveToNext()){
            SearchResultItem item = new SearchResultItem();
            item.setId(clubs.getInt(0));
            item.setDescription(clubs.getString(1));
            item.setTable("Clubs");
            item.setExtra(clubs.getString(2));
            resultItems.add(item);
        }

        clubs.close();

        while (contacts.moveToNext()){
            SearchResultItem item = new SearchResultItem();
            item.setId(contacts.getInt(0));
            item.setDescription(contacts.getString(1));
            item.setTable("Contacts");
            item.setExtra(contacts.getString(2));
            resultItems.add(item);
        }

        contacts.close();
    }
}
