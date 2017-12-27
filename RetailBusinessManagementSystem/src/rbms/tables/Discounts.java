package rbms.tables;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oracle.jdbc.internal.OracleTypes;

public class Discounts implements TableI{

	private int discnt_category;
	private double discnt_rate;
	
	/**
	 * parameterized constructor for Discounts
	 * @param rs - ResultSet
	 * @throws SQLException
	 */
	public Discounts(ResultSet rs) throws SQLException {
		this.discnt_category = rs.getInt(1);
		this.discnt_rate = rs.getDouble(2);
	}
	public Discounts() {}

	@Override
	public ResultSet getData(Connection conn) {
		try {

			// prepare to call stored function show_supplies()
			CallableStatement cs = conn.prepareCall("begin ? := rbms.show_discounts(); end;");

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
		ArrayList<Discounts> data = new ArrayList<Discounts>();

		try {
			while (rs.next()) {
				data.add(new Discounts(rs));
			}

			System.out.println("DISCNT_CATEGORY\tDISCNT_RATE");
			for (Discounts d : data) {
				System.out.print(d.discnt_category + "\t\t" + d.discnt_rate);
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
