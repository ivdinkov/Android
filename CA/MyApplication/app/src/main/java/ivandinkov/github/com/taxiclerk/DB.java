package ivandinkov.github.com.taxiclerk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Array;
import java.util.ArrayList;

/**
 * Created by iv on 06/09/2017.
 */

class DB extends SQLiteOpenHelper {
	
	
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
	
	private static final String TABLE_EXPENSE_TYPE = "expense_type";
	private static final String KEY_ID_EXPENSE_TYPE = "id";
	private static final String KEY_EXPENSE_TYPE_NAME = "expense_type_name";
	private static final String KEY_EXPENSE_TYPE_ACTIVE = "active";
	/** The Constant CREATE_EXPENSE_TYPE_TABLE. */
	private static final String CREATE_EXPENSE_TYPE_TABLE = "CREATE TABLE IF NOT EXISTS "
					+ TABLE_EXPENSE_TYPE
					+ "(" + KEY_ID_EXPENSE_TYPE + " INTEGER PRIMARY KEY,"
					+ KEY_EXPENSE_TYPE_NAME + " TEXT,"
					+ KEY_EXPENSE_TYPE_ACTIVE + " TEXT)";
	
	private static final String TABLE_EXPENSE = "expense";
	private static final String KEY_ID_EXPENSE = "id";
	private static final String KEY_EXP_DATE = "expense_date";
	private static final String KEY_EXP_PAYMENT_TYPE = "expense_payment_type";
	private static final String KEY_EXP_AMOUNT = "expense_amount";
	private static final String KEY_EXP_NOTES = "expense_notes";
	private static final String KEY_EXP_PROVIDER = "expense_type";
	private static final String KEY_EXP_IMG = "expense_img";
	/** The Constant CREATE_EXPENSE_TABLE. */
	private static final String CREATE_EXPENSE_TABLE = "CREATE TABLE IF NOT EXISTS "
					+ TABLE_EXPENSE
					+ "(" + KEY_ID_EXPENSE + " INTEGER PRIMARY KEY,"
					+ KEY_EXP_DATE + " TEXT,"
					+ KEY_EXP_PAYMENT_TYPE + " TEXT,"
					+ KEY_EXP_AMOUNT + " TEXT,"
					+ KEY_EXP_NOTES + " TEXT,"
					+ KEY_EXP_IMG + " TEXT,"
					+ KEY_EXP_PROVIDER + " TEXT)";
	
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
	
	
	
	private Cursor cursor;
	
	
	public DB(Context context, SQLiteDatabase.CursorFactory factory) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(CREATE_PROVIDER_TABLE);
			db.execSQL(CREATE_EXPENSE_TYPE_TABLE);
			db.execSQL(CREATE_EXPENSE_TABLE);
			db.execSQL(CREATE_INCOME_TABLE);
		} catch (Exception e) {
			Log.e(TAG, "DB onCreate: ", e);
		}
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROVIDER);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE_TYPE);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCOME);
		} catch (Exception e) {
			Log.e(TAG, "DB onUpgrade: ", e);
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
			long success = db.insert(TABLE_PROVIDER, null, values);
			db.close();
			if(success != -1){
				return true;
			}
		}catch(Exception e){
			Log.e(TAG, "DB insertProvider: ", e);
		}
		return false;
	}
	/** Get All Provider*/
	/**
	 * Gets provider names.	 *
	 * @return the all providers
	 */
	public ArrayList<Provider> getAllProviders() {
		ArrayList<Provider> provList = new ArrayList<Provider>();
		
		// Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_PROVIDER + " WHERE " + KEY_PR_ACTIVE + " LIKE 'yes'";
		
		SQLiteDatabase db = this.getReadableDatabase();
		try{
			cursor = db.rawQuery(selectQuery, null);
		}catch(Exception e){
			Log.e(TAG, "DB getAllProviders exception", e);
		}
		 
		
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
		try{
			cursor = db.rawQuery(selectQuery, null);
		}catch(Exception e){
			Log.e(TAG, "DB getProviderNames: ", e);
		}
		
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
	 *  EXPENSE TYPES
	 *
	 */
	/** Save provider*/
	public boolean insertExpenseType(Expense expense){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_EXPENSE_TYPE_NAME, expense.getName());
		values.put(KEY_EXPENSE_TYPE_ACTIVE, expense.getActive());
		
		try {
			long success = db.insert(TABLE_EXPENSE_TYPE, null, values);
			db.close();
			if(success != -1){
				return true;
			}
		}catch(Exception e){
			Log.e(TAG, "DB Insert New Expense: ", e);
		}
		return false;
	}
	/** Get All Provider*/
	/**
	 * Gets provider names.
	 *
	 * @return the all providers
	 */
	public ArrayList<Expense> GetExpenseTypeList() {
		ArrayList<Expense> expList = new ArrayList<>();
		
		// Select All Query
		String selectQuery = "SELECT * FROM "
						+ TABLE_EXPENSE_TYPE
						+ " WHERE "
						+ KEY_EXPENSE_TYPE_ACTIVE + " LIKE 'yes'";
		
		SQLiteDatabase db = this.getReadableDatabase();
		try{
			cursor = db.rawQuery(selectQuery, null);
		}catch(Exception e){
			Log.e(TAG, "DB GetExpenseTypeList: ", e);
		}
		
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			
			do {
				Expense exp = new Expense();
				
				exp.setID(Integer.parseInt(cursor.getString(0)));
				Log.i(TAG,cursor.getString(0));
				
				exp.setName(cursor.getString(1));
				Log.i(TAG,cursor.getString(1));
				
				exp.setActive(cursor.getString(2));
				Log.i(TAG,cursor.getString(2));
				
				// Adding contact to list
				expList.add(exp);
			} while (cursor.moveToNext());
		}
		
		db.close();
		return expList;
	}
	
	/** Get All Expense Names*/
	/**
	 * Gets provider names.
	 *
	 * @return the all providers
	 */
	public ArrayList<String> getExpenseTypeNames() {
		ArrayList<String> expList = new ArrayList<String>();
		
		// Select All Query
		String selectQuery = "SELECT " + KEY_EXPENSE_TYPE_NAME
						+ " FROM "
						+ TABLE_EXPENSE_TYPE
						+ " WHERE " + KEY_EXPENSE_TYPE_ACTIVE + " LIKE 'yes'";
		
		SQLiteDatabase db = this.getReadableDatabase();
		try {
			cursor = db.rawQuery(selectQuery, null);
		}catch(Exception e){
			Log.e(TAG, "DB getExpenseTypeNames: ", e);
		}
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				// Adding names to list
				expList.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		
		db.close();
		return expList;
	}
	/**
	 * Income
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
			Log.e(TAG, "DB saveNewIncome: ", e);
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
		try{
			cursor = db.rawQuery(selectQuery, null);
		}catch(Exception e){
			Log.e(TAG, "DB getAllIncome: ", e);
		}
		
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
	/**
	 * Expense
	 */
	public long saveNewExpense(MExpense expense) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.clear();
		values.put(KEY_EXP_DATE, expense.getDate());
		values.put(KEY_EXP_PAYMENT_TYPE, expense.getExpPayType());
		values.put(KEY_EXP_AMOUNT, expense.getAmount());
		values.put(KEY_EXP_PROVIDER, expense.getExpenseType());
		values.put(KEY_EXP_NOTES, expense.getNote());
		values.put(KEY_EXP_IMG, expense.getImage());
		
		
		// Inserting Row
		long result = 0;
		try {
			result = db.insert(TABLE_EXPENSE, null, values);
		} catch (Exception e) {
			Log.e(TAG, "DB saveNewExpense: ", e);
		}
		
		// Closing database connection
		db.close();
		return result;
	}
	
	public ArrayList<MExpense> getAllExpenses() {
		ArrayList<MExpense> expenseList = new ArrayList<MExpense>();
		// Select All Query
		String selectQuery = "SELECT "
						+ KEY_ID_EXPENSE + ","
						+ KEY_EXP_DATE + ","
						+ KEY_EXP_PAYMENT_TYPE + ","
						+ KEY_EXP_AMOUNT + ","
						+ KEY_EXP_PROVIDER + ","
						+ KEY_EXP_NOTES + ","
						+ KEY_EXP_IMG + " FROM "
						+ TABLE_EXPENSE + " ORDER BY "
						+ KEY_ID_EXPENSE;
		
		SQLiteDatabase db = this.getWritableDatabase();
		try{
			cursor = db.rawQuery(selectQuery, null);
		}catch (Exception e)
		{
			Log.e(TAG, "DB getAllExpenses: ", e);
		}
		
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				MExpense exp = new MExpense();
				exp.setID(Integer.parseInt(cursor.getString(0)));
				exp.setDate(cursor.getString(1));
				exp.setExpPayType(cursor.getString(2));
				exp.setAmount(cursor.getString(3));
				exp.setExpenseType(cursor.getString(4));
				exp.setNote(cursor.getString(5));
				exp.setImage(cursor.getString(6));
				// Adding contact to list
				expenseList.add(exp);
			} while (cursor.moveToNext());
		}
		db.close();
		// return income list
		return expenseList;
	}
	
	/**
	 *
	 * Reports
	 *
	 */
	public String getDayAmount(String date,String type) {
		String cashTotal = "";
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT SUM(CAST("
						+ KEY_INC_AMOUNT
						+ " as DECIMAL(9,2))) FROM "
						+ TABLE_INCOME
						+ " WHERE " + KEY_INC_TYPE + " LIKE '" + type
						+ "' AND " + KEY_INC_DATE + " LIKE '%" + date + "%'";
		Log.i(TAG,sql);
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				cashTotal = String.valueOf(cursor.getDouble(0));
				Log.i(TAG,cashTotal);
			} while (cursor.moveToNext());
		}
		db.close();
		cursor.close();
		return cashTotal;
	}
	
	public String getDailyExp(String date) {
		String total = "";
		
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT SUM(CAST("
						+ KEY_EXP_AMOUNT
						+ " as DECIMAL(9,2))) FROM "
						+ TABLE_EXPENSE
						+ " WHERE " + KEY_EXP_DATE + " LIKE '%" + date + "%'";
		
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				total = String.valueOf(cursor.getDouble(0));
				Log.i(TAG,total);
			} while (cursor.moveToNext());
		}
		db.close();
		cursor.close();
		return total;
	}
	
	public int allJobsForDate(String date) {
		SQLiteDatabase db = this.getReadableDatabase();
		int count = 0;
		try {
			Cursor c = null;
			String query = "SELECT * FROM " + TABLE_INCOME + " WHERE " + KEY_INC_DATE + " LIKE '%" + date + "'";
			c = db.rawQuery(query,null);
			c.moveToFirst();
			count = c.getCount();
			c.close();
		} catch (Exception e) {
			Log.e("TC", "DB ERROR: ", e);
		}
		db.close();
		return count;
	}
}
