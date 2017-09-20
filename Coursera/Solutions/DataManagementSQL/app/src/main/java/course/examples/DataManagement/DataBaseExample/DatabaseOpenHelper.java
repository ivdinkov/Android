package course.examples.DataManagement.DataBaseExample;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
	
	final static String TABLE_NAME = "artists";
	final static String AUTHOR_NAME = "name";
	final static String ID = "_id";
	final static String DOB = "dob";
	final static String NUM_BOOKS = "num_of_books";
	final static String[] columns = { ID, AUTHOR_NAME, DOB, NUM_BOOKS};

	final private static String CREATE_CMD ="CREATE TABLE artists ("+ ID	+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+ AUTHOR_NAME + " TEXT NOT NULL, "	+ DOB + " TEXT NOT NULL ," + NUM_BOOKS + " TEXT NOT NULL)";

	final private static String NAME = "artist_db";
	final private static Integer VERSION = 1;
	final private Context mContext;

	public DatabaseOpenHelper(Context context) {
		super(context, NAME, null, VERSION);
		this.mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_CMD);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// N/A
	}

	void deleteDatabase() {
		mContext.deleteDatabase(NAME);
	}
}
