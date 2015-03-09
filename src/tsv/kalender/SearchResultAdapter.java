package tsv.kalender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Antec on 30.10.2014.
 */
public class SearchResultAdapter extends ArrayAdapter<SearchResultItem> {

    ArrayList<SearchResultItem> items;

    public SearchResultAdapter(Context context, ArrayList<SearchResultItem> items) {
        super(context, R.layout.row_search);

        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        SearchResultItem item = items.get(position);

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) ApplicationContextProvider.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.row_search, null);
        }

        TextView top = (TextView)v.findViewById(R.id.search_row_top);
        TextView info = (TextView)v.findViewById(R.id.search_row_info);
        ImageView image = (ImageView)v.findViewById(R.id.search_row_image);

        if(item.getTable().equals("Dates")){
            image.setImageResource(R.drawable.ic_action_go_to_today_blue);
        }

        if (item.getTable().equals("Clubs")){
            image.setImageResource(R.drawable.ic_launcher_light_blue_small);
        }

        if(item.getTable().equals("Contacts")){
            image.setImageResource(R.drawable.ic_action_person_blue);
        }

        top.setText(item.getDescription());
        info.setText(item.getExtra());

        return v;
    }

    @Override
    public int getCount(){
        return items.size();
    }
}
