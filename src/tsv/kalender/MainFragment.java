package tsv.kalender;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainFragment extends Fragment {

    private Callbacks mCallbacks = sCallbacks;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        public void callShortcut(int id, int sport);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sCallbacks = new Callbacks() {

        @Override
        public void callShortcut(int id, int sport) {

        }
    };

    int i;
    Spinner sp;

    View rootView;
    int content = 0;

    int[] images;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.main_fragment, container, false);

        LinearLayout main_list = (LinearLayout) rootView.findViewById(R.id.main_list);

        KenBurnsView kenburn = (KenBurnsView) rootView.findViewById(R.id.kenburns_background);

        images = new int[]{
                R.drawable.picture0,
                R.drawable.picture1,
                R.drawable.picture2,
                R.drawable.picture3,
                R.drawable.picture4,
                R.drawable.picture5,
                R.drawable.picture6,
                R.drawable.picture7,
                R.drawable.picture8,
                R.drawable.picture9
        };


        int imageRand = (int) (Math.random() * images.length);
        int firstImageInt = images[imageRand];
        int secondImageInt = images[imageRand];

        if (imageRand < images.length - 1) {
            secondImageInt = images[imageRand + 1];
        } else {
            secondImageInt = images[0];
        }


        kenburn.setResourceIds(firstImageInt, secondImageInt);


        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH) + 1;
        int date = calendar.get(calendar.DAY_OF_MONTH);

        int position = 0;

        String y;
        String m;
        String d;

        ArrayList<Dates> dates = ApplicationContextProvider.getDates();
        Time b = new Time();
        Time a = new Time();

        for (Dates day : dates) {

            if(position == 10){
                break;
            }

            if (day.getStartDate().length() == 0) {
                continue;
            }

            String tmp;
            d = day.getStartDate().substring(0, day.getStartDate().indexOf("."));
            tmp = day.getStartDate().substring(day.getStartDate().indexOf(".") + 1, day.getStartDate().length());
            m = tmp.substring(0, tmp.indexOf("."));
            y = tmp.substring(tmp.indexOf(".") + 1, tmp.length());

            a.setToNow();
            b.setToNow();
            b.year = Integer.parseInt(y);
            b.month = Integer.parseInt(m)-1;
            b.monthDay = Integer.parseInt(d);
            b.hour = 23;
            b.minute = 59;
            b.second = 59;

            if(a.after(b)){
                continue;
            }

            CardView item = new CardView(getActivity());
            item = (CardView) inflater.inflate(R.layout.main_card, null);

            TextView tv = (TextView) item.findViewById(R.id.mainCardTopText);
            tv.setText(day.getDescription());
            tv = (TextView) item.findViewById(R.id.mainCardLeft);
            tv.setText(day.getLocation());
            tv = (TextView) item.findViewById(R.id.mainCardMiddle);
            tv.setText(day.getDate());
            tv = (TextView) item.findViewById(R.id.mainCardRight);
            tv.setText(day.getDay());
            main_list.addView(item, main_list.getChildCount());

            final int id = day.get_id();
            final int sport = day.getSportId();
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallbacks.callShortcut(id, sport);
                }
            });

            position++;
        }


        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException(
                    "Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;

    }

    @Override
    public void onDetach() {
        super.onDetach();

        mCallbacks = sCallbacks;
    }


}
