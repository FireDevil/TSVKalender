package tsv.kalender;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

		int size;
		ArrayList<String> arr;
		boolean cal;
		int content;
    boolean contacts = false;

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			
			Fragment fragment = new ContentFragment();
			Bundle args = new Bundle();
			args.putInt("content",content);
			args.putString("id", ""+position);
			args.putString("cal", ""+cal);
			
			if(!cal){
                if(contacts){
                    args.putBoolean("con",contacts);
                }else{
                    args.putInt("club", position);
                }
			}


			
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {

			return arr.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {

			return arr.get(position);
		}
		
		public void setArr(ArrayList<String> arr) {
			this.arr = arr;
		}

		public void setCAL(boolean cal) {
			this.cal = cal;
			
		}
		
		public void setContent(int content) {
			this.content = content;
		}

		public void setId(String id) {
			this.id = id;
		}

    public void setContacts(boolean contacts) {
        this.contacts = contacts;
    }

		String id;
	
	}
