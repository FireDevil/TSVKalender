package tsv.kalender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ClubAdapter extends ArrayAdapter<Club> {

    private ArrayList<Club> items;
    public int order;

    public ClubAdapter(Context context, ArrayList<Club> objects, int order, int content) {
        super(context, R.layout.row_contacts, objects);

        this.order = order;
        this.items = objects;

//		if(content > 0){
//			int i = 0;
//			while(i<items.size()){
//				if(items.get(i).getSportId() != content){
//					items.remove(i);
//				}else{
//					i++;
//				}
//			}
//		}
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

        Club c = items.get(position);
        if (c != null) {
            TextView tt = (TextView) v.findViewById(R.id.rowTopText);
            TextView rt = (TextView) v.findViewById(R.id.rowRight);
            TextView mt = (TextView) v.findViewById(R.id.rowMiddle);
            TextView lt = (TextView) v.findViewById(R.id.rowLeft);

            switch (order) {
                case 0:
                    top = c.getName();
                    left = c.getSport();
                    middle = c.getInternet();
                    break;
                case 1:
                    top = c.getSport();
                    left = c.getName();
                    middle = c.getInternet();
                    break;
                case 2:
                    top = c.getInternet();
                    left = c.getName();
                    middle = c.getSport();
                    break;
                case 3:
                    top = c.getName();
                    left = c.getSport();
                    middle = c.getInternet();
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
