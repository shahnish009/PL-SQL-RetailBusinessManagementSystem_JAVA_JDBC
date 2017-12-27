package rbms.tables;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oracle.jdbc.internal.OracleTypes;

public class Logs implements TableI {

	private int log;
	private String user_name, operation, op_time, table_name, tuple_pkey;
	
	/**
	 * Parameterized constructor for Logs
	 * @param rs - ResultSet
	 * @throws SQLException
	 */
	public Logs(ResultSet rs) throws SQLException {
		this.log = rs.getInt(1);
		this.user_name = rs.getString(2);
		this.operation = rs.getString(3);
		this.op_time = (rs.getString(4)).substring(0, 10);
		this.table_name = rs.getString(5);
		this.tuple_pkey = rs.getString(6);
	}
	public Logs() {}

	@Override
	public ResultSet getData(Connection conn) {
		try {

			// prepare to call stored function show_supplies()
			CallableStatement cs = conn.prepareCall("begin ? := rbms.show_logs(); end;");

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
		ArrayList<Logs> data = new ArrayList<Logs>();

		try {
			while (rs.next()) {
				data.add(new Logs(rs));
			}

			System.out.println("LOG#\tUSER_NAME\tOPERATION\tOP_TIME\t\tTABLE_NAME\tTUPLE_PKEY");
			for (Logs l : data) {
				System.out.print(l.log + "\t" + l.user_name + "\t\t" + l.operation);
				System.out.print("\t" + l.op_time + "\t\t" + l.table_name + "\t" + l.tuple_pkey);
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
