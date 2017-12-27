package rbms.tables;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oracle.jdbc.internal.OracleTypes;

public class Employees implements TableI{

	private String eid, name, telephone, email;
	
	/**
	 * Parameterized constructor for Employees
	 * @param rs - ResultSet
	 * @throws SQLException
	 */
	public Employees(ResultSet rs) throws SQLException {
		this.eid = rs.getString(1);
		this.name = rs.getString(2);
		this.telephone = rs.getString(3);
		this.email = rs.getString(4);	
	}

	public Employees() {}

	@Override
	public ResultSet getData(Connection conn) {
		try {
			
			//prepare to call stored function show_employees()
			CallableStatement cs = conn.prepareCall("begin ? := rbms.show_employees(); end;");
			
			//register the out parameter
			cs .registerOutParameter(1, OracleTypes.CURSOR);
			
			//execute and retrieve the results set
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

		ArrayList<Employees> data = new ArrayList<Employees>();
		
		try {
			while (rs.next()) {
				data.add(new Employees(rs));
			}
			
			System.out.println("EID\tNAME\tTELEPHONE#\tEMAIL");
			for(Employees e: data) {
				System.out.print(e.eid + "\t" + e.name);
				System.out.print("\t" + e.telephone + "\t" + e.email);
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
