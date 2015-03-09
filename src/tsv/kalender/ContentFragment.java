package tsv.kalender;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A fragment representing a single Calendar detail screen. This fragment is
 * either contained in a {@link ListingActivity} in two-pane mode (on
 * tablets) or a {@link ContentDetailActivity} on handsets.
 */
public class ContentFragment extends Fragment {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
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
        public ArrayList<Dates> getDates();

        public ArrayList<Club> getClubs();

        public ArrayList<Contact> getContacts();
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {


        @Override
        public ArrayList<Dates> getDates() {
            return null;
        }

        @Override
        public ArrayList<Club> getClubs() {
            return null;
        }

        @Override
        public ArrayList<Contact> getContacts() {
            return null;
        }


    };


    public ContentFragment() {
    }

    boolean twoPane = false;
    boolean cal;
    int content;
    int id;

    DBHelper database;
    SQLiteDatabase db;

    ArrayList<Contact> contacts;
    ArrayList<Dates> dates;

    String internet;
    int _id;
    String city;
    String street;
    String top;
    String sub1;
    String sub2;
    String text1;
    String text2;
    String text3;
    String text4;

    ArrayList<Club> clubs = new ArrayList<Club>();
    ArrayList<Adress> adresses = new ArrayList<Adress>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_fragment,
                container, false);

        if (getActivity().findViewById(R.id.twoPager) == null) {
            twoPane = true;
        }

        content = getArguments().getInt("content");

        if (getArguments().getString("id").equals("null")) {
            id = 0;
        } else {
            id = Integer.parseInt(getArguments().getString("id"));
        }

        cal = getArguments().getString("cal").equals("true");

        dates = mCallbacks.getDates();
        contacts = ApplicationContextProvider.getContacts();
        clubs = mCallbacks.getClubs();
        adresses = ApplicationContextProvider.getAdresses();

//        if(dates.size() == 0 || clubs.size()==0){
//            dates = ApplicationContextProvider.getDates();
//            clubs = ApplicationContextProvider.getClubs();
//        }

        ContentDetailActivity.setArrays(dates,clubs,contacts);

        if (getArguments().containsKey("club")) {

            if(getArguments().containsKey("steady")){
                clubs = ApplicationContextProvider.getSteadyClubs();
                dates = ApplicationContextProvider.getSteadyDates();
            }

            setImage(rootView, clubs.get(getArguments().getInt("club")).getSportId());
            top = clubs.get(getArguments().getInt("club")).getName();
            sub1 = clubs.get(getArguments().getInt("club")).getSport();
            sub2 = clubs.get(getArguments().getInt("club")).getInternet();

            text1 = contacts.get(clubs.get(getArguments().getInt("club")).getContactId()).getName();
            text2 = contacts.get(clubs.get(getArguments().getInt("club")).getContactId()).getFunction();
            text3 = contacts.get(clubs.get(getArguments().getInt("club")).getContactId()).getMail();
            _id = clubs.get(getArguments().getInt("club")).getAdressId();

            String postcode = adresses.get(_id).getPostCode();
            String extra = adresses.get(_id).getExtra();
            city = adresses.get(_id).getCity();
            street = adresses.get(_id).getStreet();
            String adress = "" + street + "\n" + postcode + "  " + city + "\n" + extra;

            _id = contacts.get(clubs.get(getArguments().getInt("club")).getContactId()).get_id();

            TextView h1 = (TextView) rootView.findViewById(R.id.content_heading);
            h1.setText(top);

            TextView h2 = (TextView) rootView.findViewById(R.id.content_subheading1);
            h2.setText(sub1);

            TextView h3 = (TextView) rootView.findViewById(R.id.content_subheading2);
            h3.setText(sub2);
            h3.setTextColor(Color.parseColor("#0b63f0"));
            h3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uri = String.format("http://" + sub2);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                }

            });

            TextView i1 = (TextView) rootView.findViewById(R.id.content_label_series);
            TextView i12 = (TextView) rootView.findViewById(R.id.content_hint_series);
            i12.setText(getResources().getString(R.string.content_hint_name));
            i1.setText(text1);
            i1.setTextColor(Color.parseColor("#0b63f0"));

            if (text1.equals("Ohne Angabe")) {

            } else {
                i1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ApplicationContextProvider.getContext(), ContentDetailActivity.class);
                        intent.putExtra("id", "" + _id);
                        intent.putExtra("cal", "false");
                        intent.putExtra("content", content);
                        intent.putExtra("con", "true");
                        startActivity(intent);
                    }

                });
            }


            TextView i2 = (TextView) rootView.findViewById(R.id.content_label_starters);
            TextView i22 = (TextView) rootView.findViewById(R.id.content_hint_starters);
            i22.setText(getResources().getString(R.string.content_hint_function));
            i2.setText(text2);

            TextView i3 = (TextView) rootView.findViewById(R.id.content_label_form);
            TextView i32 = (TextView) rootView.findViewById(R.id.content_hint_form);
            i32.setText(getResources().getString(R.string.content_hint_mail));
            i3.setText(text3);
            i3.setTextColor(Color.parseColor("#0b63f0"));
            ;
            i3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{contacts.get(id).getMail()});
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, getResources().getString(R.string.email_intent));

                    startActivity(Intent.createChooser(emailIntent, "Email"));
                }

            });

            TextView i4 = (TextView) rootView.findViewById(R.id.content_label_club);
            TextView i42 = (TextView) rootView.findViewById(R.id.content_hint_club);
            i42.setText(getResources().getString(R.string.content_hint_adress));
            i4.setText(adress);
            i4.setTextColor(Color.parseColor("#0b63f0"));
            i4.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uri = String.format("geo:0,0?q=" + city + " , " + street);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                }

            });

            TextView i5 = (TextView) rootView.findViewById(R.id.content_label_extra);
            i5.setVisibility(View.GONE);
            TextView i52 = (TextView) rootView.findViewById(R.id.content_hint_extra);
            i52.setText(getResources().getString(R.string.content_hint_extra));
            i52.setVisibility(View.GONE);

            return rootView;
        }


        if (cal) {

            clubs = ApplicationContextProvider.getClubs();

            if(getArguments().containsKey("steady")){
                clubs = ApplicationContextProvider.getSteadyClubs();
                dates = ApplicationContextProvider.getSteadyDates();
            }

            int clubId = dates.get(id).getClubId();
            int contactId = clubs.get(clubId).getContactId();

            setImage(rootView, dates.get(id).getSportId());
            text1 = clubs.get(clubId).getName();
            text2 = clubs.get(clubId).getInternet();
            text3 = contacts.get(contactId).getName();
            text4 = contacts.get(contactId).getMail();

            String club = "" + text1 + "\n\n" + text2 + "\n\n" + text3 + "\n\n" + text4 + "\n\n" + adresses.get(clubs.get(dates.get(id).getClubId()).getAdressId()).getExtra();

            TextView h1 = (TextView) rootView.findViewById(R.id.content_heading);
            h1.setText(dates.get(id).getDescription());

            TextView h2 = (TextView) rootView.findViewById(R.id.content_subheading1);
            h2.setText(dates.get(id).getLocation());
            h2.setTextColor(Color.parseColor("#0b63f0"));
            h2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uri = String.format("geo:0,0?q=" + dates.get(id).getLocation());
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                }

            });

            TextView h3 = (TextView) rootView.findViewById(R.id.content_subheading2);
            if(dates.get(id).getDate() == null || dates.get(id).getDate().equals("")){
                h3.setText(dates.get(id).getDay());
            }else{
                h3.setText(dates.get(id).getDate() + " / " + dates.get(id).getDay());
            }


            TextView i1 = (TextView) rootView.findViewById(R.id.content_label_series);
            i1.setText(dates.get(id).getSeries());

            TextView i2 = (TextView) rootView.findViewById(R.id.content_label_starters);
            i2.setText(dates.get(id).getStarters());

            TextView i3 = (TextView) rootView.findViewById(R.id.content_label_form);
            i3.setText(dates.get(id).getForm());

            TextView i4 = (TextView) rootView.findViewById(R.id.content_label_club);
            i4.setText(club);
            i4.setTextColor(Color.parseColor("#0b63f0"));
            i4.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ApplicationContextProvider.getContext(), ContentDetailActivity.class);
                    intent.putExtra("id", "" + id);
                    intent.putExtra("cal", "" + cal);
                    intent.putExtra("content", content);
                    intent.putExtra("club", dates.get(id).getClubId());
                    ContentDetailActivity.setArrays(dates, clubs,contacts);
                    startActivity(intent);
                }

            });

            TextView i5 = (TextView) rootView.findViewById(R.id.content_label_extra);
            i5.setVisibility(View.GONE);
            TextView i52 = (TextView) rootView.findViewById(R.id.content_hint_extra);
            i52.setText(getResources().getString(R.string.content_hint_extra));
            i52.setVisibility(View.GONE);

        } else {

            if (getArguments().containsKey("con")) {
                contacts = mCallbacks.getContacts();
            }

            setImage(rootView, contacts.get(id).getSportId());
            text1 = adresses.get(contacts.get(id).getAdressId()).getStreet();
            text2 = adresses.get(contacts.get(id).getAdressId()).getCity();
            text3 = adresses.get(contacts.get(id).getAdressId()).getPostCode();
            text4 = adresses.get(contacts.get(id).getAdressId()).getExtra();

            String adress = "" + text1 + "\n" + text3 + "  " + text2 + "\n";//+text4;

            TextView h1 = (TextView) rootView.findViewById(R.id.content_heading);
            h1.setText(contacts.get(id).getName());

            TextView h2 = (TextView) rootView.findViewById(R.id.content_subheading1);
            h2.setText(contacts.get(id).getFunction());

            TextView h3 = (TextView) rootView.findViewById(R.id.content_subheading2);
            h3.setText(contacts.get(id).getMail());
            h3.setTextColor(Color.parseColor("#0b63f0"));
            h3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{contacts.get(id).getMail()});
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, getResources().getString(R.string.email_intent));

                    startActivity(Intent.createChooser(emailIntent, "Email"));
                }

            });

            TextView i1 = (TextView) rootView.findViewById(R.id.content_label_series);
            i1.setText(contacts.get(id).getTel());
            TextView i12 = (TextView) rootView.findViewById(R.id.content_hint_series);
            i12.setText(getResources().getString(R.string.content_hint_tel));
            i1.setTextColor(Color.parseColor("#0b63f0"));
            i1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + contacts.get(id).getTel()));
                    startActivity(intent);
                }

            });

            TextView i2 = (TextView) rootView.findViewById(R.id.content_label_starters);
            i2.setText(contacts.get(id).getMobil());
            i2.setTextColor(Color.parseColor("#0b63f0"));
            TextView i22 = (TextView) rootView.findViewById(R.id.content_hint_starters);
            i22.setText(getResources().getString(R.string.content_hint_mob));
            i2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + contacts.get(id).getMobil()));
                    startActivity(intent);
                }

            });

            TextView i3 = (TextView) rootView.findViewById(R.id.content_label_form);
            i3.setText(contacts.get(id).getFax());
            TextView i32 = (TextView) rootView.findViewById(R.id.content_hint_form);
            i32.setText(getResources().getString(R.string.content_hint_fax));

            TextView i4 = (TextView) rootView.findViewById(R.id.content_label_club);
            i4.setText(adress);
            i4.setTextColor(Color.parseColor("#0b63f0"));
            TextView i42 = (TextView) rootView.findViewById(R.id.content_hint_club);
            i42.setText(getResources().getString(R.string.content_hint_adress));
            i4.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uri = String.format("geo:0,0?q=" + city + " , " + street);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                }

            });


            TextView i5 = (TextView) rootView.findViewById(R.id.content_label_extra);
            i5.setText("ITEM5 - Info");
            i5.setVisibility(View.GONE);
            TextView i52 = (TextView) rootView.findViewById(R.id.content_hint_extra);
            i52.setText(getResources().getString(R.string.content_hint_extra));
            i52.setVisibility(View.GONE);

        }

        return rootView;
    }

    public void setImage(View rootView, int content) {

        ImageView iv = (ImageView) rootView.findViewById(R.id.contentImage);

        switch (content) {
            case 0:
                iv.setImageResource(R.drawable.ic_launcher);
                break;
            case 1:
                iv.setImageResource(R.drawable.lauf_black);
                break;
            case 2:
                iv.setImageResource(R.drawable.nk_black);
                break;
            case 3:
                iv.setImageResource(R.drawable.bia_black);
                break;
            case 4:
                iv.setImageResource(R.drawable.alpin_black);
                break;
            case 5:
                iv.setImageResource(R.drawable.ic_action_person);
                break;
        }
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

        mCallbacks = sDummyCallbacks;
    }
}
