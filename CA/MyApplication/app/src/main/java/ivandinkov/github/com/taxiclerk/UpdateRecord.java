package ivandinkov.github.com.taxiclerk;

/**
 * Created by iv on 07/09/2017.
 */

interface UpdateRecord {
	/**
	 * On record select update.
	 *
	 * @param recordID the record id
	 * @param flag the flag
	 */
	public void onRecordSelectUpdate(int recordID, int flag);
	
}
