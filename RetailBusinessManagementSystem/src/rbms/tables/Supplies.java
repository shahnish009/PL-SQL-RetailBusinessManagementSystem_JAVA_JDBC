package rbms.tables;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oracle.jdbc.internal.OracleTypes;

public class Supplies implements TableI {
	private int sup, quantity;
	private String pid, sid, sdate;
	
	/**
	 * parameterized constructor for supplies
	 * @param rs - ResultSet
	 * @throws SQLException
	 */
	public Supplies(ResultSet rs) throws SQLException {
		this.sup = rs.getInt(1);
		this.pid = rs.getString(2);
		this.sid = rs.getString(3);
		this.sdate = (rs.getString(4)).substring(0, 10);
		this.quantity = rs.getInt(5);
	}
	public Supplies() {}

	@Override
	public ResultSet getData(Connection conn) {
		try {

			// prepare to call stored function show_supplies()
			CallableStatement cs = conn.prepareCall("begin ? := rbms.show_supplies(); end;");

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
		ArrayList<Supplies> data = new ArrayList<Supplies>();

		try {
			while (rs.next()) {
				data.add(new Supplies(rs));
			}

			System.out.println("SUP#\tPID\tSID\tSDATE\t\tQUANTITY");
			for (Supplies s : data) {
				System.out.print(s.sup + "\t" + s.pid + "\t" + s.sid);
				System.out.print("\t" + s.sdate + "\t" + s.quantity);
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
