package tsv.kalender;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A list fragment representing a list of Dates. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link ContentFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ListingFragment extends Fragment {

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
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
		public void onItemSelected(String id);
		public boolean getCal();
		public void setDateList(ArrayList<Dates> d);
		public ArrayList<Dates> getDates();
        public ArrayList<Dates> getDates(String where);
		public ArrayList<Club> getClubs();
        public ArrayList<Club> getClubs(String where);
        public ArrayList<Contact> getContacts();
        public ArrayList<Contact> getContacts(String where);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(String id) {
		}

		@Override
		public boolean getCal() {
			return false;
		}

		@Override
		public void setDateList(ArrayList<Dates> d){
		}

		@Override
		public ArrayList<Dates> getDates() {
			return null;
		}

        @Override
        public ArrayList<Dates> getDates(String where) {
            return null;
        }

        @Override
		public ArrayList<Club> getClubs() {
			return null;
		}

        @Override
        public ArrayList<Club> getClubs(String where) {
            return null;
        }

        @Override
        public ArrayList<Contact> getContacts() {
            return null;
        }

        @Override
        public ArrayList<Contact> getContacts(String where) {
            return null;
        }
    };

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ListingFragment() {
	}

	ArrayList<Club> clubs;
	ArrayList<Dates> dates;
    ArrayList<Contact> contacts;

	int content;
	boolean cal;
	int order = 0;

	private ActionMode mActionMode;
	DatesAdapter datesadapter;
	ClubAdapter clubadapter;
    ContactAdapter contactAdapter;

	DBHelper database;

    View rootView;
	ListView lv;
	Menu mMenu;

    private int mActionBarTitleColor;
    private int mActionBarHeight;
    private int mHeaderHeight;
    private int mMinHeaderTranslation;
    private KenBurnsView mHeaderPicture;
    private ImageView mHeaderLogo;
    private View mHeader;
    private View mPlaceHolderView;
    private AccelerateDecelerateInterpolator mSmoothInterpolator;

    private RectF mRect1 = new RectF();
    private RectF mRect2 = new RectF();

    private AlphaForegroundColorSpan mAlphaForegroundColorSpan;
    private SpannableString mSpannableString;

    private TypedValue mTypedValue = new TypedValue();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.list_fragement,container, false);
        mSmoothInterpolator = new AccelerateDecelerateInterpolator();
        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        mMinHeaderTranslation = -mHeaderHeight + getActionBarHeight();

        mHeader = rootView.findViewById(R.id.header);
        mHeaderPicture = (KenBurnsView) rootView.findViewById(R.id.header_picture);
        mHeaderPicture.setResourceIds(R.drawable.picture0, R.drawable.picture7);
        mHeaderLogo = (ImageView) rootView.findViewById(R.id.header_logo);

        mActionBarTitleColor = getResources().getColor(android.R.color.white);

        mSpannableString = new SpannableString(getActivity().getActionBar().getTitle());
        mAlphaForegroundColorSpan = new AlphaForegroundColorSpan(mActionBarTitleColor);

        lv = (ListView)rootView.findViewById(R.id.listView);
        lv.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {


                mCallbacks.onItemSelected(""+(int)arg3);
            }

        });

        setupActionBar();
        setupListView();



        cal = mCallbacks.getCal();
		content = ApplicationContextProvider.getIndex();

        dates = new ArrayList<Dates>();
        clubs = new ArrayList<Club>();
        contacts = new ArrayList<Contact>();
        dates= mCallbacks.getDates();
        clubs=mCallbacks.getClubs();
        contacts = mCallbacks.getContacts();


		if(dates.size() == 0 && clubs.size() == 0 && !getArguments().containsKey("contacts")){

			TextView tv = new TextView(ApplicationContextProvider.getContext());
			tv.setTextColor(Color.parseColor("#000000"));
			tv.setTextSize(20);
			tv.setText(getResources().getString(R.string.no_entries));
            tv.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);


			FrameLayout ll =(FrameLayout)rootView.findViewById(R.id.list_ll);
			ll.addView(tv);

		}

        if(getArguments().containsKey("contacts")){
            contactAdapter = new ContactAdapter(getActivity(),contacts,order,content);
            lv.setAdapter(contactAdapter);

            mHeaderLogo.setImageResource(R.drawable.ic_action_person);
            return rootView;
        }

		if(cal){
			datesadapter = new DatesAdapter(getActivity(),dates,order,content);
			datesadapter.setAct(getActivity());

            mHeaderLogo.setImageResource(R.drawable.ic_action_go_to_today);
			lv.setAdapter(datesadapter);
		}else{

			clubadapter = new ClubAdapter(getActivity(),clubs,order,content);
			lv.setAdapter(clubadapter);
		}

		return rootView;
	}

    private void setupListView() {

        mPlaceHolderView = getActivity().getLayoutInflater().inflate(R.layout.fake_header, lv, false);
        lv.addHeaderView(mPlaceHolderView);
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int scrollY = getScrollY();
                //sticky actionbar
                mHeader.setTranslationY(Math.max(-scrollY, mMinHeaderTranslation));
                //header_logo --> actionbar icon
                float ratio = clamp(mHeader.getTranslationY() / mMinHeaderTranslation, 0.0f, 1.0f);
                interpolate(mHeaderLogo, getActionBarIconView(), mSmoothInterpolator.getInterpolation(ratio));
                //actionbar title alpha
                //getActionBarTitleView().setAlpha(clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F));
                //---------------------------------
                //better way thanks to @cyrilmottier
                setTitleAlpha(clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F));
            }
        });
    }

    private void setTitleAlpha(float alpha) {
        mAlphaForegroundColorSpan.setAlpha(alpha);
        mSpannableString.setSpan(mAlphaForegroundColorSpan, 0, mSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getActivity().getActionBar().setTitle(mSpannableString);
    }

    public static float clamp(float value, float max, float min) {
        return Math.max(Math.min(value, min), max);
    }

    private void interpolate(View view1, View view2, float interpolation) {
        getOnScreenRect(mRect1, view1);
        getOnScreenRect(mRect2, view2);

        float scaleX = 1.0F + interpolation * (mRect2.width() / mRect1.width() - 1.0F);
        float scaleY = 1.0F + interpolation * (mRect2.height() / mRect1.height() - 1.0F);
        float translationX = 0.5F * (interpolation * (mRect2.left + mRect2.right - mRect1.left - mRect1.right));
        float translationY = 0.5F * (interpolation * (mRect2.top + mRect2.bottom - mRect1.top - mRect1.bottom));

        view1.setTranslationX(translationX);
        view1.setTranslationY(translationY - mHeader.getTranslationY());
        view1.setScaleX(scaleX);
        view1.setScaleY(scaleY);
    }

    private RectF getOnScreenRect(RectF rect, View view) {
        rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        return rect;
    }

    public int getScrollY() {
        View c = lv.getChildAt(0);
        if (c == null) {
            return 0;
        }

        int firstVisiblePosition = lv.getFirstVisiblePosition();
        int top = c.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = mPlaceHolderView.getHeight();
        }

        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }

    private void setupActionBar() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setIcon(new ColorDrawable(android.R.color.transparent));

        //getActionBarTitleView().setAlpha(0f);
    }

    private ImageView getActionBarIconView() {
        return (ImageView) getActivity().findViewById(android.R.id.home);
    }

    /*private TextView getActionBarTitleView() {
        int id = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
        return (TextView) findViewById(id);
    }*/

    public int getActionBarHeight() {
        if (mActionBarHeight != 0) {
            return mActionBarHeight;
        }
        getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue, true);
        mActionBarHeight = TypedValue.complexToDimensionPixelSize(mTypedValue.data, getResources().getDisplayMetrics());
        return mActionBarHeight;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.order, menu);

        super.onCreateOptionsMenu(menu,menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        /*if(item.getItemId()== R.id.order){
            String where="SELECT * FROM ";
            if(cal){
                if(content > 0){
                    where = where+" Dates WHERE sportId="+content;
                }else{
                    where = where+" Dates";
                }

                switch(order){
                    case 0:
                        where=where +" ORDER BY location";
                        break;
                    case 1:
                        where=where +" ORDER BY description";
                        break;
                    case 2:
                        where=where +" ORDER BY startDate";
                        break;
                    case 3:
                        where=where +" ORDER BY _id";
                        break;
                }



                datesadapter = new DatesAdapter(getActivity(),mCallbacks.getDates(where),order,content);
                datesadapter.setAct(getActivity());
                lv.setAdapter(datesadapter);
                if(order == 3){
                 order =-1;
                }
            }else{
                if(content > 0){
                    where = where+" Clubs WHERE sportId="+content;
                }else{
                    where = where+" Clubs";
                }
                switch(order){
                    case 0:
                        where=where +" ORDER BY name";
                        break;
                    case 1:
                        where=where +" ORDER BY sport";
                        break;
                    case 2:
                        where=where +" ORDER BY internet";
                        break;
                    case 3:
                        where=where +" ORDER BY _id";
                        break;
                }

                clubadapter = new ClubAdapter(getActivity(),mCallbacks.getClubs(where),order,content);
                lv.setAdapter(clubadapter);
                if(order == 3){
                    order =-1;
                }
            }
            order++;
        }*/

        return true;
    }

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

        setupActionBar();
		mCallbacks = (Callbacks) activity;

	}

	@Override
	public void onDetach() {
		super.onDetach();

		mCallbacks = sDummyCallbacks;
	}

    @Override
    public void onResume(){
        super.onResume();
        setupActionBar();
    }

	public void selectItem(int id){
		lv.getChildAt(id)
		.findViewById(R.id.image)
		.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT));
	}


	public ListView getListView(){
		return lv;
	}

	public DatesAdapter getAdapter() {
		return datesadapter;
	}

	public void setAdapter(DatesAdapter adapter) {
		this.datesadapter = adapter;
	}



}
