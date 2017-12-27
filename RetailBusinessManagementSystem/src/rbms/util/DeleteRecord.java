package rbms.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class DeleteRecord {
	private DatabaseConnection dc = new DatabaseConnection();
	
	/**
	 * select a table from which the record needs to be deleted
	 * @param conn
	 * @param sc
	 */
	public void selectTable(Connection conn, Scanner sc) {
		System.out.println("Delete record menu:");
		System.out.println("1. Purchases");
		System.out.println("\nPlease enter your choice: ");
		System.out.println();

		int choice = -1;
		String choiceStr = null;
		try {
			choiceStr = sc.nextLine();
			choice = Integer.parseInt(choiceStr);
			if (choice < 1 || choice > 1) {
				throw new RuntimeException("Invalid choice entered");
			}
		} catch (Exception e) {
			dc.closeDatabaseConnection(conn);
			System.out.println("Exception " + e + " occurred!");
			e.printStackTrace();
			System.exit(1);
		}

		switch (choice) {
		case 1:
			deleteFromPurchases(conn, sc);
			break;

		default:
			dc.closeDatabaseConnection(conn);
			System.out.println("Invalid choice entered!");
			System.exit(1);
		}
	}
	
	/**
	 * delete from purchases
	 * @param conn
	 * @param sc
	 */
	private void deleteFromPurchases(Connection conn, Scanner sc) {

		try {
			CallableStatement cs = null;

			System.out.println("Please enter PUR# (Format: ######): ");
			int pur = Integer.parseInt(sc.nextLine());
			if (pur > 999999 ||  pur < 100000) {
				throw new RuntimeException("Invalid PUR# entered!");
			}
			
			// prepare to call stored procedure
			cs = conn.prepareCall("begin rbms.delete_purchase(:1); end;");

			// set the in parameters
			cs.setInt(1, pur);

			// execute the stored procedure
			cs.executeQuery();
			cs.close();

			System.out.println("Deletion successfull!");
		} catch (SQLException e) {
			dc.closeDatabaseConnection(conn);
			System.out.println("\n*** SQLException caught ***\n" + e.getMessage());
			System.exit(1);
		} catch (Exception e) {
			dc.closeDatabaseConnection(conn);
			System.out.println("\n*** Exception caught ***\n" + e);
			System.exit(1);
		}
		
	}

}
