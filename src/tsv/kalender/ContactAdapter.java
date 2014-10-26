package tsv.kalender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Antec on 20.05.2014.
 */
public class ContactAdapter extends ArrayAdapter<Contact> {

    private ArrayList<Contact> items;
    public int order;

    public ContactAdapter(Context context, ArrayList<Contact> objects, int order, int content) {
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
            v = vi.inflate(R.layout.row_contacts, null);
        }

        Contact c = items.get(position);
        if (c != null) {
            TextView tt = (TextView) v.findViewById(R.id.rowTopText);
            TextView rt = (TextView) v.findViewById(R.id.rowRight);
            TextView mt = (TextView) v.findViewById(R.id.rowMiddle);
            TextView lt = (TextView) v.findViewById(R.id.rowLeft);

            switch (order) {
                case 0:
                    top = c.getName();
                    left = c.getFunction();
                    middle = c.getMail();
                    break;
                case 1:
                    top = c.getFunction();
                    left = c.getName();
                    middle = c.getMail();
                    break;
                case 2:
                    top = c.getMail();
                    left = c.getName();
                    middle = c.getFunction();
                    break;
            }

            if (tt != null) {
                tt.setText(top);
            }
            if (lt != null) {
                lt.setText(left);
            }
            if (mt != null) {
                mt.setText(middle);
            }
            rt.setVisibility(View.GONE);

        }
        return v;
    }

    public void setOrder(int order) {
        this.order = order;
    }



}
