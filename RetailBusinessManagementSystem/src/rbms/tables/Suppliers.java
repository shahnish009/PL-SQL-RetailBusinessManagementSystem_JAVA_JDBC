package rbms.tables;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oracle.jdbc.internal.OracleTypes;

public class Suppliers implements TableI {
	private String sid, name, city, telephone, email;
	
	/**
	 * parameterized constructor for 
	 * @param rs - ResultSet
	 * @throws SQLException
	 */
	public Suppliers(ResultSet rs) throws SQLException {
		this.sid = rs.getString(1);
		this.name = rs.getString(2);
		this.city = rs.getString(3);
		this.telephone = rs.getString(4);
		this.email = rs.getString(5);
	}
	public Suppliers() {}

	@Override
	public ResultSet getData(Connection conn) {
		try {

			// prepare to call stored function show_suppliers()
			CallableStatement cs = conn.prepareCall("begin ? := rbms.show_suppliers(); end;");

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
		ArrayList<Suppliers> data = new ArrayList<Suppliers>();

		try {
			while (rs.next()) {
				data.add(new Suppliers(rs));
			}

			System.out.println("SID\tNAME\t\tCITY\tTELEPHONE#\t\tEMAIL");
			for (Suppliers s : data) {
				System.out.print(s.sid + "\t" + s.name + "   \t" + s.city);
				System.out.print("\t\t" + s.telephone + "\t" + s.email);
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
