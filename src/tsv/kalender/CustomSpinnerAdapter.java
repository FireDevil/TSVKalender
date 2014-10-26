package tsv.kalender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomSpinnerAdapter extends ArrayAdapter<String>{
	
	LayoutInflater inflater;

		public CustomSpinnerAdapter(Context context, int textViewResourceId,
                                    String[] objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}

		public View getCustomView(int position, View convertView, ViewGroup parent) {

			View row=inflater.inflate(R.layout.spinnerrow, parent, false);

			ImageView icon=(ImageView)row.findViewById(R.id.icon);
			TextView tv = (TextView)row.findViewById(R.id.itemtext);
			LinearLayout ll = (LinearLayout)row.findViewById(R.id.spinnerll);

			switch(position){
			case 0:
				icon.setImageResource(R.drawable.tsv);
				tv.setText("Thüringer Skiverband");
			break;
			case 1:
				icon.setImageResource(R.drawable.lauf_black);
				tv.setText("Skilanglauf");
			break;
			case 2:
				icon.setImageResource(R.drawable.nk_black);
				tv.setText("Sprung /Nord. Kombination");
			break;
			case 3:
				icon.setImageResource(R.drawable.bia_black);
				tv.setText("Biathlon");
			break;
			case 4:
				icon.setImageResource(R.drawable.sprung_black);
                tv.setText("Alpin");
			break;
			default:
				icon.setImageResource(R.drawable.tsv);
				tv.setText("Thüringer Skiverband");
			break;
			}

			return row;
		}

		public void setInflater(LayoutInflater inflater) {
			this.inflater = inflater;
		}
	}
