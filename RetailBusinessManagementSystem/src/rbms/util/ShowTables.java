package rbms.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Scanner;

import rbms.tables.Customers;
import rbms.tables.Discounts;
import rbms.tables.Employees;
import rbms.tables.Logs;
import rbms.tables.Products;
import rbms.tables.Purchases;
import rbms.tables.Suppliers;
import rbms.tables.Supplies;

public class ShowTables {
	private DatabaseConnection dc = new DatabaseConnection();

	/**
	 * select table to show
	 * @param conn
	 * @param sc
	 */
	public void selectTable(Connection conn, Scanner sc) {
		// TODO Auto-generated method stub
		System.out.println("Table menu:");
		System.out.println("1. Employees");
		System.out.println("2. Customers");
		System.out.println("3. Products");
		System.out.println("4. Discounts");
		System.out.println("5. Suppliers");
		System.out.println("6. Supplies");
		System.out.println("7. Purchases");
		System.out.println("8. Logs");
		System.out.println("\nPlease enter your choice: ");
		System.out.println();

		int choice = -1;
		String choiceStr = null;
		try {
			choiceStr = sc.nextLine();
			choice = Integer.parseInt(choiceStr);
			if (choice < 1 || choice > 8) {
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
			displayEmployees(conn);
			break;

		case 2:
			displayCustomers(conn);
			break;
			
		case 3:
			displayProducts(conn);
			break;
			
		case 4:
			displayDiscounts(conn);
			break;
			
		case 5:
			displaySuppliers(conn);
			break;
			
		case 6:
			displaySupplies(conn);
			break;
			
		case 7:
			displayPurchases(conn);
			break;
			
		case 8:
			displayLogs(conn);
			break;
			
		default:
			dc.closeDatabaseConnection(conn);
			System.out.println("Invalid choice entered!");
			System.exit(1);
		}
	}

	private void displayEmployees(Connection conn) {
		Employees e = new Employees();
		ResultSet rs = e.getData(conn);
		e.displayData(rs);		
	}

	private void displayCustomers(Connection conn) {
		Customers c = new Customers();
		ResultSet rs = c.getData(conn);
		c.displayData(rs);
	}

	private void displayProducts(Connection conn) {
		Products pr = new Products();
		ResultSet rs = pr.getData(conn);
		pr.displayData(rs);
	}

	private void displayDiscounts(Connection conn) {
		Discounts d = new Discounts();
		ResultSet rs = d.getData(conn);
		d.displayData(rs);
	}

	private void displaySuppliers(Connection conn) {

		Suppliers s = new Suppliers();
		ResultSet rs = s.getData(conn);
		s.displayData(rs);
	}

	private void displaySupplies(Connection conn) {

		Supplies su = new Supplies();
		ResultSet rs = su.getData(conn);
		su.displayData(rs);
	}

	private void displayPurchases(Connection conn) {
		Purchases pu = new Purchases();
		ResultSet rs = pu.getData(conn);
		pu.displayData(rs);
	}

	private void displayLogs(Connection conn) {
		Logs l = new Logs();
		ResultSet rs = l.getData(conn);
		l.displayData(rs);
	}
}
