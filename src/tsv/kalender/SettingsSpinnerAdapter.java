package tsv.kalender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingsSpinnerAdapter extends ArrayAdapter<String>{

	LayoutInflater inflater;

	public SettingsSpinnerAdapter(Context context, int textViewResourceId,
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

		View row=inflater.inflate(R.layout.settings_row, parent, false);

		TextView tv = (TextView)row.findViewById(R.id.itemtext);
		LinearLayout ll = (LinearLayout)row.findViewById(R.id.settingsll);


		return row;
	}

	public void setInflater(LayoutInflater inflater) {
		this.inflater = inflater;
	}
}
