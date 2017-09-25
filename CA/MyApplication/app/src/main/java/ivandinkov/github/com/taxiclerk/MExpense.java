package ivandinkov.github.com.taxiclerk;

/**
 * Created by iv on 24/09/2017.
 */

public class MExpense {
	
	// private variables
	/** The _id. */
	int _id;
	
	/** The _amount. */
	String _amount;
	
	/** The _date. */
	String _date;
	
	/** The _note. */
	String _note;
	
	/** The _active. */
	String _active;
	
	/** The _type. */
	String _type;
	
	/** The _provider. */
	String _expenseType;
	
	// Empty constructor
	/**
	 * Instantiates a new income.
	 */
	public MExpense() {
		
	}
	
	// constructor
	/**
	 * Instantiates a new income.
	 *
	 * @param id the id
	 * @param date the date
	 * @param type the type
	 * @param amount the amount
	 * @param note the note
	 * @param expenseType the provider
	 */
	public MExpense(int id, String date, String type, String amount, String note, String expenseType) {
		this._id = id;
		this._date = date;
		this._type = type;
		this._amount = amount;
		this._note = note;
		this._expenseType = expenseType;
	}

	public MExpense(String date, String type, String amount, String note, String expenseType) {
		this._date = date;
		this._type = type;
		this._amount = amount;
		this._note = note;
		this._expenseType = expenseType;
	}
	
	
	// getting ID
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getID() {
		return this._id;
	}
	
	// setting id
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setID(int id) {
		this._id = id;
	}
	
	// getting date
	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public String getDate() {
		return this._date;
	}
	
	// setting date
	/**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
	public void setDate(String date) {
		this._date = date;
	}
	
	// getting type
	/**
	 * Gets the inc type.
	 *
	 * @return the inc type
	 */
	public String getIncType() {
		return this._type;
	}
	
	// setting type
	/**
	 * Sets the inc type.
	 *
	 * @param type the new inc type
	 */
	public void setIncType(String type) {
		this._type = type;
	}
	
	// getting amount
	/**
	 * Gets the amount.
	 *
	 * @return the amount
	 */
	public String getAmount() {
		return this._amount;
	}
	
	// setting amount
	/**
	 * Sets the amount.
	 *
	 * @param amount the new amount
	 */
	public void setAmount(String amount) {
		this._amount = amount;
	}
	
	// getting note
	/**
	 * Gets the note.
	 *
	 * @return the note
	 */
	public String getNote() {
		return this._note;
	}
	
	// setting date
	/**
	 * Sets the note.
	 *
	 * @param note the new note
	 */
	public void setNote(String note) {
		this._note = note;
	}
	
	// setting provider
	/**
	 * Sets the provider.
	 *
	 * @param expenseType the new provider
	 */
	public void setExpenseType(String expenseType) {
		this._expenseType = expenseType;
	}
	
	// getting provider
	/**
	 * Gets the provider.
	 *
	 * @return the provider
	 */
	public String getProvider() {
		return this._expenseType;
	}
}
