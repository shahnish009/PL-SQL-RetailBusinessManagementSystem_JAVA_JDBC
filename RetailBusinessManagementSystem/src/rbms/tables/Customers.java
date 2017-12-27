package rbms.tables;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oracle.jdbc.internal.OracleTypes;

public class Customers implements TableI{

	private int visits_made;
	private String cid, name, telephone, last_visit_date;
	
	/**
	 * parameterized constructor for customers
	 * @param rs - ResultSet
	 * @throws SQLException
	 */
	public Customers(ResultSet rs) throws SQLException {
		this.cid = rs.getString(1);
		this.name = rs.getString(2);
		this.telephone = rs.getString(3);
		this.visits_made = rs.getInt(4);
		this.last_visit_date = (rs.getString(5)).substring(0, 10);
	}
	public Customers() {}

	@Override
	public ResultSet getData(Connection conn) {
		try {

			// prepare to call stored function show_supplies()
			CallableStatement cs = conn.prepareCall("begin ? := rbms.show_customers(); end;");

			// register the out parameter
			cs.registerOutParameter(1, OracleTypes.CURSOR);

			// execute and retrieve the results set
			cs.execute();
			ResultSet rs = (ResultSet) cs.getObject(1);
			return rs;
		} catch (SQLException e) {
			System.out.println("\n*** SQLException caught ***\n" + e.getMessage());
			System.exit(1);
		} catch (Exception e) {
			System.out.println("\n*** Exception caught ***\n" + e);
			System.exit(1);
		}
		return null;
	}

	@Override
	public void displayData(ResultSet rs) {
		ArrayList<Customers> data = new ArrayList<Customers>();

		try {
			while (rs.next()) {
				data.add(new Customers(rs));
			}

			System.out.println("CID\tNAME\tTELEPHONE#\tVISITS_MADE\tLAST_VISIT_DATE");
			for (Customers c : data) {
				System.out.print(c.cid + "\t" + c.name + "\t" + c.telephone);
				System.out.print("\t\t" + c.visits_made + "\t" + c.last_visit_date);
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
