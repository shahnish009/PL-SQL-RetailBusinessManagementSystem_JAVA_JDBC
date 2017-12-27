package rbms.tables;

import java.sql.Connection;
import java.sql.ResultSet;

public interface TableI {

	/**
	 * method to get data into resultset from the database
	 * @param conn - database connection
	 * @return ResultSet
	 */
	public ResultSet getData(Connection conn);
	
	/**
	 * method to display the received data
	 * @param rs
	 */
	public void displayData (ResultSet rs);

}
