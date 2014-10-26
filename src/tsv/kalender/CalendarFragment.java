package tsv.kalender;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;

public class CalendarFragment extends Fragment {

	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onDateSelected(String id);
		public boolean getCal();
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onDateSelected(String id) {
		}

		@Override
		public boolean getCal() {
			return false;
		}

	};

	int y;
	int m;
	int d;
	String day = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Show the Up button in the action bar.
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.calendar_fragment,
				container, false);

		CalendarView cal = (CalendarView)rootView.findViewById(R.id.calendarView1);
		cal.setOnDateChangeListener(new OnDateChangeListener(){
			@Override
			public void onSelectedDayChange(CalendarView view, int year,
					int month, int dayOfMonth) {
				
				if(year != y || m != month || d != dayOfMonth){
					y = year;
					m = month+1;
					d = dayOfMonth;
					
					
					String mo = m+"";
					if(m<10){
						mo = "0"+mo;
					}
					
					String da = d+"";
					if(d<10){
						da = "0"+da;
					}
					
					if(day.equals("")){
							day = da+"."+mo+"."+y;
					}
					
					if(!day.equals(da+"."+mo+"."+y)){
						
						day = da+"."+mo+"."+y;
						mCallbacks.onDateSelected(day);
					}
				}
			}

		});

		return rootView;

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		//		 Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

}
