package ivandinkov.github.com.taxiclerk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iv on 06/09/2017.
 */

public class DB extends SQLiteOpenHelper {
	
	
	/** The Constant DATABASE_VERSION. */
	private static final int DATABASE_VERSION = 1;
	/** Database Name */
	/** The Constant DATABASE_NAME. */
	private static final String DATABASE_NAME = "taxi_book_keeper";
	/** Table names */
	/** The Constant TABLE_PROVIDER. */
	private static final String TABLE_PROVIDER = "provider";
	/** The Constant TAG. */
	private static final String TAG = "TC";
	/** The Constant KEY_ID. */
	private static final String KEY_ID = "id";
	/** The Constant KEY_NAME. */
	private static final String KEY_PROVIDER_NAME = "name";
	/** The Constant KEY_PR_ACTIVE*/
	private static final String KEY_PR_ACTIVE = "active";
	
	
	
	/** Tables */
	/** The Constant CREATE_JOB_PROVIDER_TABLE. */
	private static final String CREATE_PROVIDER_TABLE = "CREATE TABLE IF NOT EXISTS "
					+ TABLE_PROVIDER + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
					+ KEY_PROVIDER_NAME + " TEXT," + KEY_PR_ACTIVE + " TEXT)";
	
	public DB(Context context, SQLiteDatabase.CursorFactory factory) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(CREATE_PROVIDER_TABLE);
//			db.execSQL(CREATE_TRIAL_TABLE);
//			db.execSQL(CREATE_EXPENSE_TABLE);
//			db.execSQL(CREATE_INCOME_TABLE);
//			db.execSQL(CREATE_HOURS_TABLE);
//			db.execSQL(CREATE_JOB_PROVIDER_TABLE);
		} catch (Exception e) {
			Log.e(TAG, "DB onCreate exception", e);
		}
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROVIDER);
//			db.execSQL("DROP TABLE IF EXISTS" + TABLE_TRIAL);
//			db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
//			db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCOME);
//			db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOURS);
//			db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOB_PROVIDER);
		} catch (Exception e) {
			Log.e(TAG, "DB onUpgrade exception", e);
		}
		// Create tables again
		onCreate(db);
	}
	
	/**
	 *
	 *  PROVIDERS
	 *
	 */
	/** Save provider*/
	public boolean insertProvider(Provider provider){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_PROVIDER_NAME, provider.getName());
		values.put(KEY_PR_ACTIVE, provider.getActive());
		
		try {
			/** Inserting Row */
			long success = db.insert(TABLE_PROVIDER, null, values);
			/**  Close connection */
			db.close();
			if(success != -1){
				return true;
			}
		}catch(Exception e){
			// TODO Log Exception in log file
		}
		return false;
	}
	/** Get All Providers */
	/**
	 * Gets the all providers.
	 *
	 * @return the all providers
	 */
	public List<Provider> getAllProviders() {
		List<Provider> provList = new ArrayList<Provider>();
		
		// Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_PROVIDER + " WHERE " + KEY_PR_ACTIVE + " LIKE 'yes'";
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			
			do {
				Provider inc = new Provider();
				
				inc.setID(Integer.parseInt(cursor.getString(0)));
				Log.i(TAG,cursor.getString(0));
				
				inc.setName(cursor.getString(1));
				Log.i(TAG,cursor.getString(1));
				
				inc.setActive(cursor.getString(2));
				Log.i(TAG,cursor.getString(2));
				
				// Adding contact to list
				provList.add(inc);
			} while (cursor.moveToNext());
		}
		
		db.close();
		return provList;
	}
	
}
