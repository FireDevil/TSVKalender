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
 * Created by Antec on 20.05.2014.
 */
public class NavDrawerAdapter extends ArrayAdapter<String> {

    private ArrayList<String> items;
    public int order;

    public NavDrawerAdapter(Context context, ArrayList<String> objects, int content) {
        super(context, R.layout.row_contacts, objects);

        this.order = order;
        this.items = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        String top = "";
        String middle = "";
        String left = "";

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) ApplicationContextProvider.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.nav_drawer_footer, null);
        }
        String c = items.get(position);
        if (c != null) {
            TextView tt = (TextView) v.findViewById(R.id.nav_drawer_footer_text);
            ImageView image = (ImageView) v.findViewById(R.id.nav_drawer_footer_image);
            tt.setText(c);

            switch(position){
                case 0:
                    image.setImageResource(R.drawable.ic_action_view_as_grid);
                    break;
                case 1:
                    image.setImageResource(R.drawable.ic_action_go_to_today);
                    break;
                case 2:
                    image.setImageResource(R.drawable.ic_launcher_light);
                    break;
                case 3:
                    image.setImageResource(R.drawable.ic_action_person);
                    break;
                case 4:
                    image.setImageResource(R.drawable.ic_action_settings);
                    break;
            }


        }
        return v;
    }
}
