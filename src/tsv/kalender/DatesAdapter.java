package tsv.kalender;

import android.app.Activity;
import android.content.Context;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Vector;

public class DatesAdapter extends ArrayAdapter<Dates> {

    private Callbacks mCallbacks = sCallbacks;
    private ArrayList<Dates> items;
    public int order;
    public Vector<Integer> checked = new Vector<Integer>();
    Activity act;
    ActionMode mode;
    int checkedCount;
    Dates d;


    public interface Callbacks {
        public void onItemChecked(int id);
    }

    private static Callbacks sCallbacks = new Callbacks() {

        @Override
        public void onItemChecked(int id) {

        }

    };


    public DatesAdapter(Context context,
                        ArrayList<Dates> objects, int order, int content) {
        super(context, R.layout.row_dates, objects);

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
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;

        final ViewHolder holder;

        String top = "";
        String middle = "";
        String left = "";
        String right = "";


        if (v == null) {
            holder = new ViewHolder();
            LayoutInflater vi = (LayoutInflater) ApplicationContextProvider.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.row_dates, null);
            holder.cb = (CheckBox) v.findViewById(R.id.checkBox);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        d = items.get(position);

        holder.cb.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mCallbacks.onItemChecked(position+1);
            }
        });


        if (d != null) {


            holder.cb.setChecked(d.isChecked());

            TextView tt = (TextView) v.findViewById(R.id.rowTopText);
            TextView rt = (TextView) v.findViewById(R.id.rowRight);
            TextView mt = (TextView) v.findViewById(R.id.rowMiddle);
            TextView lt = (TextView) v.findViewById(R.id.rowLeft);

            switch (order) {
                case 0:
                    top = d.getDescription();
                    left = d.getLocation();
                    middle = d.getDate();
                    right = d.getDay();
                    break;
                case 1:

                    top = d.getLocation();
                    left = d.getDescription();
                    middle = d.getDate();
                    right = d.getDay();
                    break;
                case 2:
                    top = d.getDate();
                    left = d.getLocation();
                    middle = d.getDescription();
                    right = d.getDay();
                    break;
                case 3:
                    top = d.getLocation();
                    left = d.getDescription();
                    middle = d.getDate();
                    right = d.getDay();
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
            if (rt != null) {
                rt.setText(right);
            }
        }
        return v;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    class ViewHolder {
        CheckBox cb;
    }

    public void setAct(Activity act) {
        this.act = act;
        mCallbacks = (Callbacks) act;
    }

    public int getCheckedCount() {
        int i = 0;

        if (i != checkedCount) {
            for (Dates d : items) {
                if (d.isChecked()) {
                    i++;
                }
            }
        }

        checkedCount = i;
        return i;
    }

    public ArrayList<Dates> getItems() {
        return items;
    }

    public void setItems(ArrayList<Dates> items) {
        this.items = items;
    }
}
