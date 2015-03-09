package tsv.kalender;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper{
	
	public static final int DB_VERSION = 13;
	public static final String DB_NAME = "calendar.db";
    private static String DB_PATH = "/data/data/"+ApplicationContextProvider.getContext().getPackageName()+"/databases/";
	public SQLiteDatabase db;
	private final Context myContext;
	
	public DBHelper (){
		super(ApplicationContextProvider.getContext(), DB_NAME, null, DB_VERSION);
        this.myContext = ApplicationContextProvider.getContext();

        openDataBase();
        db = getWritableDatabase();
	}
	
	public Cursor select(String query) throws SQLException{
        return db.rawQuery(query, null);
	}

	public void insert(String table, ContentValues values) throws SQLException{
		db.insert(table, null, values);
	}


	public void delete(String table, String where) throws SQLException{
		db.delete(table, where, null);
	}

	public void update(String table, ContentValues values, String where){
		db.update(table, values, where, null);
	}

	public void sqlCommand(String command){
		db.execSQL(command);

	}

	public synchronized void close(){
		if(db != null){
			db.close();
		}

		super.close();
	}

    public void createDataBase() throws IOException {

        this.getReadableDatabase();

        try {

            copyDataBase();

        } catch (IOException e) {

            throw new Error("Error copying database");

        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;
        boolean check;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            check = true;
        } catch (SQLiteCantOpenDatabaseException sql){
            check = false;
        } catch(SQLiteException e){
            check = false;
        }

        if(checkDB != null){

            checkDB.close();
        }

        return check;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getResources().getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        if(checkDataBase()){
            String myPath = DB_PATH + DB_NAME;
            db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        }else{
            try {
                createDataBase();
            }catch (IOException e){
            }
        }

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    public void onUpgrade(SQLiteDatabase db,String table, int oldVersion, int newVersion){
		db.execSQL("DROP TABLE IF EXISTS "+table);
	}

	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		
	}
}
