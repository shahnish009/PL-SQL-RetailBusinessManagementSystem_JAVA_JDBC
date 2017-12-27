package rbms.util;

import java.sql.*;
import oracle.jdbc.pool.OracleDataSource;

public class DatabaseConnection {
	
	/**
	 * get connection
	 * @return
	 */
	public Connection getDatabaseConnection() {
		
		try {
			OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
			ds.setURL("jdbc:oracle:thin:@castor.cc.binghamton.edu:1521:acad111");
			Connection conn = ds.getConnection("username", "password");
			
			System.out.println("\nEstablished connection with database!\n");
			return conn;
		} catch (SQLException ex) {
			System.out.println ("\n*** SQLException caught ***\n" + ex);
			ex.printStackTrace();
			System.exit(1);
		} catch (Exception e) {
			System.out.println ("\n*** Exception caught ***\n" + e);
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}
	
	/**
	 * close connection
	 * @param conn
	 */
	public void closeDatabaseConnection(Connection conn) {
		try {
			conn.close();
			System.out.println("\nDatabase connection closed!\n");
		} catch (SQLException e) {
			System.out.println("\n*** SQLException caught ***\n" + e);
			e.printStackTrace();
			System.exit(1);
		}
		
	}
}
