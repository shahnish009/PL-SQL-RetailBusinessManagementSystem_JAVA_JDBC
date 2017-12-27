package rbms.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import oracle.jdbc.internal.OracleTypes;
import rbms.tables.TableI;

public class MonthlySaleActivities implements TableI {
	
	private String eid, name, month;
	private int year, times_sales, qty_sold;
	private double total_sold;
	private DatabaseConnection dc = new DatabaseConnection();
	private Scanner sc = null;

	public MonthlySaleActivities(ResultSet rs) throws SQLException {
		this.eid = rs.getString(1);
		this.name = rs.getString(2);
		this.month = rs.getString(3);
		this.year = rs.getInt(4);
		this.times_sales = rs.getInt(5);
		this.qty_sold = rs.getInt(6);
		this.total_sold = rs.getInt(7);
	}

	public MonthlySaleActivities() {}
		
	public void selectTable(Connection conn, Scanner sc) {
		this.sc = sc;
		
		System.out.println("Monthly sale activities menu:");
		System.out.println("1. Employees");
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
			ResultSet rs = getData(conn);
			displayData(rs);	
			break;

		default:
			dc.closeDatabaseConnection(conn);
			System.out.println("Invalid choice entered!");
			System.exit(1);
		}
	}

	@Override
	public ResultSet getData(Connection conn) {
		try {

			System.out.println("Please enter EID (Format: e##): ");
			String eidIn = sc.nextLine();
			if (eidIn.length() != 3) {
				throw new RuntimeException("Invalid EID entered!");
			}
			
			// prepare to call stored function show_employees()
			CallableStatement cs = conn.prepareCall("{call rbms.monthly_sale_activities(?,?)}");

			// set the in parameters
			cs.setString(1, eidIn);
			
			// register the out parameter
			cs.registerOutParameter(2, OracleTypes.CURSOR);

			// execute and retrieve the results set
			cs.execute();
			ResultSet rs = (ResultSet) cs.getObject(2);
			return rs;
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
		return null;
	}

	@Override
	public void displayData(ResultSet rs) {

		ArrayList<MonthlySaleActivities> data = new ArrayList<MonthlySaleActivities>();

		try {
			while (rs.next()) {
				data.add(new MonthlySaleActivities(rs));
			}

			System.out.println("EID\tNAME\tMONTH\tYEAR\tTIMES_SALES\tQTY_SOLD\tTOTAL_SOLD");
			for (MonthlySaleActivities e : data) {
				System.out.print(e.eid + "\t" + e.name);
				System.out.print("\t" + e.month + "\t" + e.year);
				System.out.print("\t\t" + e.times_sales + "\t\t" + e.qty_sold);
				System.out.print("\t" + e.total_sold);
				System.out.println();
			}
		} catch (SQLException e) {
			System.out.println("\n*** SQLException caught ***\n" + e.getMessage());
			System.exit(1);
		} catch (Exception e) {
			System.out.println("\n*** Exception caught ***\n" + e);
			System.exit(1);
		}
	}
}
