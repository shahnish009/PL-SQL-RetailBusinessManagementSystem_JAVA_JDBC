package rbms.tables;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oracle.jdbc.internal.OracleTypes;

public class Products implements TableI {

	private int qoh, qoh_threshold, discnt_category;
	private double original_price;
	private String pid, name;
	
	/**'
	 * parameterized constructor for products
	 * @param rs - ResultSet
	 * @throws SQLException
	 */
	public Products(ResultSet rs) throws SQLException {
		this.pid = rs.getString(1);
		this.name = rs.getString(2);
		this.qoh = rs.getInt(3);
		this.qoh_threshold = rs.getInt(4);
		this.original_price = rs.getDouble(5);
		this.discnt_category = rs.getInt(6);
	}
	public Products() {}

	@Override
	public ResultSet getData(Connection conn) {
		try {

			// prepare to call stored function show_supplies()
			CallableStatement cs = conn.prepareCall("begin ? := rbms.show_products(); end;");

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
		ArrayList<Products> data = new ArrayList<Products>();

		try {
			while (rs.next()) {
				data.add(new Products(rs));
			}

			System.out.println("PID\tNAME\t\tQOH\tQOH_THRESHOLD\tORIGINAL_PRICE\tDISCNT_CATEGORY");
			for (Products pr : data) {
				System.out.print(pr.pid + "\t" + pr.name + "\t\t" + pr.qoh);
				System.out.print("\t\t" + pr.qoh_threshold + "\t\t" + pr.original_price + "\t\t" + pr.discnt_category);
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
