package rbms.driver;

import java.sql.Connection;
import java.util.Scanner;

import rbms.util.*;

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//get the database connection
		DatabaseConnection dc = new DatabaseConnection();
		Connection conn = dc.getDatabaseConnection();

		Scanner sc = new Scanner(System.in);
		int choice = -1;
		String choiceStr = null;
		
		boolean status = true;

		ShowTables st = new ShowTables();
		InsertRecord ir = new InsertRecord();
		DeleteRecord dr = new DeleteRecord();
		MonthlySaleActivities msa = new MonthlySaleActivities();
		PurchaseSavings ps = new PurchaseSavings();

		while (status) {
			System.out.println("Main menu:");
			System.out.println("1. Show Table");
			System.out.println("2. Insert a Record into Table");
			System.out.println("3. Delete a Record from Table");
			System.out.println("4. Monthly Sale Activities");
			System.out.println("5. Purchase Savings");			
			System.out.println("\nPlease enter your choice: ");
			System.out.println();

			try {
				choiceStr = sc.nextLine();
				choice = Integer.parseInt(choiceStr);
				if (choice < 1 || choice > 5) {
					throw new RuntimeException("Invalid choice entered");
				}
			} catch (Exception e) {
				dc.closeDatabaseConnection(conn);
				System.out.println("Exception " + e + " occurred!");
				e.printStackTrace();
				System.exit(1);
			}

			//based on choice, select the operation to perform
			switch (choice) {

			case 1:
				st.selectTable(conn, sc);
				break;
				
			case 2:
				ir.selectTable(conn, sc);
				break;
				
			case 3:
				dr.selectTable(conn, sc);
				break;
				
			case 4:
				msa.selectTable(conn, sc);
				break;
				
			case 5:
				ps.getPurchaseSavings(conn, sc);
				break;
				
			default:
				dc.closeDatabaseConnection(conn);
				System.out.println("Invalid choice entered!");
				System.exit(1);
			}

			System.out.println("\n\nDo you wish to continue? Please enter Y/N\n");
			String nextChoice = sc.nextLine();

			if (nextChoice.equalsIgnoreCase("Y") || nextChoice.equalsIgnoreCase("YES")) {
				continue;
			} else {
				status = false;
				sc.close();
				dc.closeDatabaseConnection(conn);
			}
		}
	}

}
