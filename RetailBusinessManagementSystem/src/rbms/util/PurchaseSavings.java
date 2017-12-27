package rbms.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Scanner;

public class PurchaseSavings {
	
	private DatabaseConnection dc = new DatabaseConnection();
	
	/**
	 * get purchase saving
	 * @param conn
	 * @param sc
	 */
	public void getPurchaseSavings(Connection conn, Scanner sc) {

		try {
			CallableStatement cs = null;

			System.out.println("Please enter PUR# (Format: ######): ");
			int pur = Integer.parseInt(sc.nextLine());
			if (pur > 999999 ||  pur < 100000) {
				throw new RuntimeException("Invalid PUR# entered!");
			}
			
			// prepare to call stored procedure
			cs = conn.prepareCall("{? = call rbms.purchase_saving(?)}");

			 //register the out parameter
			cs.registerOutParameter(1, Types.DOUBLE);
			
			// set the in parameters
			cs.setInt(2, pur);
			
//			// register the out parameter
//			cs.registerOutParameter(1, Types.DOUBLE);

			// execute the stored procedure
			cs.executeUpdate();
			
			double savings = cs.getDouble(1);
			
			System.out.println("TOTAL SAVING");
			System.out.println(savings);
			
			cs.close();

		} catch (SQLException e) {
			dc.closeDatabaseConnection(conn);
			System.out.println("\n*** SQLException caught ***\n" + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		} catch (Exception e) {
			dc.closeDatabaseConnection(conn);
			System.out.println("\n*** Exception caught ***\n" + e);
			System.exit(1);
		}
		
	}
}
