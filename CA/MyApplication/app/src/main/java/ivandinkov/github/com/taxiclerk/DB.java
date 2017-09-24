package ivandinkov.github.com.taxiclerk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

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
	/** The Constant TAG. */
	private static final String TAG = "TC";
	
	
	private static final String TABLE_PROVIDER = "provider";
	private static final String KEY_ID = "id";
	private static final String KEY_PROVIDER_NAME = "name";
	private static final String KEY_PR_ACTIVE = "active";
	/** The Constant CREATE_JOB_PROVIDER_TABLE. */
	private static final String CREATE_PROVIDER_TABLE = "CREATE TABLE IF NOT EXISTS "
					+ TABLE_PROVIDER
					+ "(" + KEY_ID + " INTEGER PRIMARY KEY,"
					+ KEY_PROVIDER_NAME + " TEXT,"
					+ KEY_PR_ACTIVE + " TEXT)";
	
	
	private static final String TABLE_INCOME = "income";
	private static final String KEY_ID_INCOME = "id";
	private static final String KEY_INC_DATE = "income_date";
	private static final String KEY_INC_TYPE = "income_type";
	private static final String KEY_INC_AMOUNT = "income_amount";
	private static final String KEY_INC_NOTES = "income_notes";
	private static final String KEY_INC_PROVIDER = "income_provider";
	/** The Constant CREATE_INCOME_TABLE. */
	private static final String CREATE_INCOME_TABLE = "CREATE TABLE IF NOT EXISTS "
					+ TABLE_INCOME
					+ "(" + KEY_ID_INCOME + " INTEGER PRIMARY KEY,"
					+ KEY_INC_DATE + " TEXT,"
					+ KEY_INC_TYPE + " TEXT,"
					+ KEY_INC_AMOUNT + " TEXT,"
					+ KEY_INC_NOTES + " TEXT,"
					+ KEY_INC_PROVIDER + " TEXT)";
	
	
	
	
	
	public DB(Context context, SQLiteDatabase.CursorFactory factory) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(CREATE_PROVIDER_TABLE);
//			db.execSQL(CREATE_TRIAL_TABLE);
//			db.execSQL(CREATE_EXPENSE_TABLE);
			db.execSQL(CREATE_INCOME_TABLE);
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
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCOME);
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
	/** Get All Provider*/
	/**
	 * Gets provider names.
	 *
	 * @return the all providers
	 */
	public ArrayList<Provider> getAllProviders() {
		ArrayList<Provider> provList = new ArrayList<Provider>();
		
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
	
	/** Get All Provider Names*/
	/**
	 * Gets provider names.
	 *
	 * @return the all providers
	 */
	public ArrayList<String> getProviderNames() {
		ArrayList<String> provList = new ArrayList<String>();
		
		// Select All Query
		String selectQuery = "SELECT " + KEY_PROVIDER_NAME + " FROM " + TABLE_PROVIDER + " WHERE " + KEY_PR_ACTIVE + " LIKE 'yes'";
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				// Adding names to list
				provList.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		
		db.close();
		return provList;
	}
	
	/**
	 *
	 * Income
	 *
	 */
	public long saveNewIncome(Income income) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.clear();
		values.put(KEY_INC_DATE, income.getDate());
		values.put(KEY_INC_TYPE, income.getIncType());
		values.put(KEY_INC_AMOUNT, income.getAmount());
		values.put(KEY_INC_PROVIDER, income.getProvider());
		values.put(KEY_INC_NOTES, income.getNote());
		
		
		// Inserting Row
		long result = 0;
		try {
			result = db.insert(TABLE_INCOME, null, values);
		} catch (Exception e) {
			Log.e("APP", "exception", e);
		}
		
		// Closing database connection
		db.close();
		return result;
	}
	
	public ArrayList<Income> getAllIncome() {
		ArrayList<Income> incomeList = new ArrayList<Income>();
		// Select All Query
		String selectQuery = "SELECT "
						+ KEY_ID_INCOME + ","
						+ KEY_INC_DATE + ","
						+ KEY_INC_TYPE + ","
						+ KEY_INC_AMOUNT + ","
						+ KEY_INC_PROVIDER + ","
						+  KEY_INC_NOTES + " FROM "
						+ TABLE_INCOME + " ORDER BY "
						+ KEY_ID_INCOME;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Income inc = new Income();
				inc.setID(Integer.parseInt(cursor.getString(0)));
				inc.setDate(cursor.getString(1));
				inc.setIncType(cursor.getString(2));
				inc.setAmount(cursor.getString(3));
				inc.setProvider(cursor.getString(4));
				inc.setNote(cursor.getString(5));
				// Adding contact to list
				incomeList.add(inc);
			} while (cursor.moveToNext());
		}
		db.close();
		// return income list
		return incomeList;
	}
	
}
