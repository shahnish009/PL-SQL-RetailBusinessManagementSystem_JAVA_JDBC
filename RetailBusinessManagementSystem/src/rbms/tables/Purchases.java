package rbms.tables;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oracle.jdbc.internal.OracleTypes;

public class Purchases implements TableI {

	private int pur, qty;
	private double total_price;
	private String eid, pid, cid, ptime;
	
	/**
	 * parameterized constructor for purchases
	 * @param rs
	 * @throws SQLException
	 */
	public Purchases(ResultSet rs) throws SQLException {
		this.pur = rs.getInt(1);
		this.eid = rs.getString(2);
		this.pid = rs.getString(3);
		this.cid = rs.getString(4);
		this.qty = rs.getInt(5);
		this.ptime = (rs.getString(6)).substring(0, 10);
		this.total_price = rs.getDouble(7);
	}
	public Purchases() {}

	@Override
	public ResultSet getData(Connection conn) {
		try {

			// prepare to call stored function show_supplies()
			CallableStatement cs = conn.prepareCall("begin ? := rbms.show_purchases(); end;");

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
		ArrayList<Purchases> data = new ArrayList<Purchases>();

		try {
			while (rs.next()) {
				data.add(new Purchases(rs));
			}

			System.out.println("PUR#\tEID\tPID\tCID\tQTY\tPTIME\t\tTOTAL_PRICE");
			for (Purchases pu : data) {
				System.out.print(pu.pur + "\t" + pu.eid + "\t" + pu.pid);
				System.out.print("\t" + pu.cid + "\t" + pu.qty + "\t" + pu.ptime + "\t" + pu.total_price);
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
