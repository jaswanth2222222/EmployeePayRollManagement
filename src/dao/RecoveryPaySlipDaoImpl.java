package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RecoveryPaySlipDaoImpl implements RecoveryPaySlipDao {

	// Query Syntax for Copying the data from table to table
	private static final String COPY_PAYSLIPS_QUERY = "insert into recovery_payslips select * from payslips "
			+ "where employee_id = ?";
	// initialising the Connection Class to call getConnection() Method it can be
	// used in any of the method
	final DataBaseConnection dataBaseConnection = new DataBaseConnection();

	@Override
	public boolean movePaySlips(String employeeId) {

		// initialising the PaySlipDaoImplementation to call methods inside it
		PaySlipDaoImpl paySlipDaoImpl = new PaySlipDaoImpl();

		try (Connection connection = dataBaseConnection.getDataBaseConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(COPY_PAYSLIPS_QUERY);) {
			// Prepared Statement using Connection and Setting the dynamic values which are
			// received as arguments
			preparedStatement.setString(1, employeeId);
			// Storing number of rows affected after executing the query
			int rowsInserted = preparedStatement.executeUpdate();
			// Checking whether the query is executed properly or not
			if (rowsInserted > 0) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println("An Error Occurred while Removing the Employee Details" + e.getMessage());
			throw new RuntimeException(e);
		}
		return false;
	}
}
