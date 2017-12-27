package rbms.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Scanner;

public class InsertRecord {
	
	private DatabaseConnection dc = new DatabaseConnection();
	
	/**
	 * table to select for insertion
	 * @param conn
	 * @param sc
	 */
	public void selectTable(Connection conn, Scanner sc) {
		System.out.println("Insert record menu:");
		System.out.println("1. Customers");
		System.out.println("2. Purchases");
		System.out.println("\nPlease enter your choice: ");
		System.out.println();

		int choice = -1;
		String choiceStr = null;
		try {
			choiceStr = sc.nextLine();
			choice = Integer.parseInt(choiceStr);
			if (choice < 1 || choice > 2) {
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
			insertIntoCustomers(conn, sc);
			break;

		case 2:
			insertIntoPurchases(conn, sc);
			break;

		default:
			dc.closeDatabaseConnection(conn);
			System.out.println("Invalid choice entered!");
			System.exit(1);
		}
	}

	/**
	 * insert into customers
	 * @param conn
	 * @param sc
	 */
	private void insertIntoCustomers(Connection conn, Scanner sc) {

		try {
			CallableStatement cs = null;

			System.out.println("Please enter CID (Format: c###): ");
			String cid = sc.nextLine();
			if (cid.length() != 4) {
				throw new RuntimeException("Invalid CID entered!");
			}
			System.out.println("Please enter NAME (Format: FirstName): ");
			String name = sc.nextLine();
			if (name.length() > 15) {
				throw new RuntimeException("NAME should be at most 15 characters!");
			}
			System.out.println("Please enter TELEPHONE# (Format: ###-###-####): ");
			String telephone = sc.nextLine();
			if (telephone.length() != 12) {
				throw new RuntimeException("Invalid TELEPHONE# entered");
			}

			// prepare to call stored procedure
			cs = conn.prepareCall("begin rbms.add_customer(:1,:2,:3); end;");

			// set the in parameters
			cs.setString(1, cid);
			cs.setString(2, name);
			cs.setString(3, telephone);

			// execute the stored procedure
			cs.executeQuery();
			cs.close();

			System.out.println("Insertion successfull!");
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

	/**
	 * insert into purchases
	 * @param conn
	 * @param sc
	 */
	private void insertIntoPurchases(Connection conn, Scanner sc) {

		DatabaseConnection dc = new DatabaseConnection();

		try {
			CallableStatement cs = null;

			System.out.println("Please enter EID (Format: e##): ");
			String eid = sc.nextLine();
			if (eid.length() != 3) {
				throw new RuntimeException("Invalid EID entered!");
			}
			System.out.println("Please enter PID (Format: p###): ");
			String pid = sc.nextLine();
			if (pid.length() != 4) {
				throw new RuntimeException("Invalid PID entered!");
			}
			System.out.println("Please enter CID (Format: c###): ");
			String cid = sc.nextLine();
			if (cid.length() != 4) {
				throw new RuntimeException("Invalid CID entered");
			}
			System.out.println("Please enter QTY ( < 100000): ");
			int qty = Integer.parseInt(sc.nextLine());
			if (qty > 99999) {
				throw new RuntimeException("Invalid QTY entered");
			}

			// prepare to call stored procedure
			cs = conn.prepareCall("begin rbms.add_purchase(:1,:2,:3,:4); end;");

			// set the in parameters
			cs.setString(1, eid);
			cs.setString(2, pid);
			cs.setString(3, cid);
			cs.setInt(4, qty);

			// execute the stored procedure
			cs.executeQuery();
			CallableStatement cst = conn.prepareCall("{call dbms_output.get_line(?, ?)}");
			cst.registerOutParameter(1, Types.VARCHAR);
			cst.registerOutParameter(2, Types.INTEGER);
			
			while (true) {
				cst.execute();
				if (cst.getInt(2) == 0) {
					String line = cst.getString(1);
					System.out.println(line);
				} else {
					break;
				}
				
			}
			cst.close();
			cs.close();

			System.out.println("Insertion successfull!");
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
