package tsv.kalender;

import android.provider.BaseColumns;

public class Base implements BaseColumns{

	public static final String AM_TABLE = "android_metadata";
	public static final String DATES_TABLE ="Dates";
	public static final String CONTACTS_TABLE ="Contacts";
	public static final String CLUB_TABLE = "Clubs";
	public static final String ADRESS_TABLE="Adresses";
	
	public static final String ID = "_id INTEGER PRIMARY KEY";
	public static final String NAME = "name TEXT";
	public static final String DESCRIPTION = "description TEXT";
	public static final String SERIES = "series TEXT";
	public static final String START = "start TEXT";
	public static final String END = "end TEXT";
	public static final String STARTERS = "starters TEXT";
	public static final String SYST = "sysTime TEXT";
	public static final String LOCATION = "location TEXT";
	public static final String DAY = "day TEXT";
	public static final String FORM = "form TEXT";
	public static final String SportId = "sportId NUMERIC";
	
	public static final String ClubId = "clubId NUMERIC";
	public static final String SPORT = "sport TEXT";
	public static final String INTERNET ="internet TEXT";
	
	public static final String ContactId = "contactId NUMERIC";
	public static final String FUNCTION ="function TEXT";
	public static final String MAIL = "mail TEXT";
	public static final String TEL = "tel TEXT";
	public static final String MOB = "mobil TEXT";
	public static final String FAX = "fax TEXT";
	
	public static final String AdressId = "adressId NUMERIC";
	public static final String STREET ="street TEXT";
	public static final String POSTCODE = "postcode TEXT";
	public static final String CITY ="city TEXT";
	public static final String EXTRA = "extra TEXT";
	
	public static final String STATE = "state NUMERIC";
	public static final String COMMA = ", ";
	
	public static final String CALENDAR_TABLE = "Calendar";
	public static final String SELECTED = "selected TEXT";
	public static final String CalId = "calId NUMERIC";






}
