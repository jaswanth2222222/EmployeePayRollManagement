package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecoveryPaySlipDaoImpl implements RecoveryPaySlipDao {

	// Query Syntax for Copying the data from table to table
	private static final String COPY_PAYSLIPS_QUERY = "insert into recovery_payslips select * from payslips "
			+ "where employee_id = ?";
	// initialising the Connection Class to call getConnection() Method it can be
	// used in any of the method
	final DataBaseConnection dataBaseConnection = new DataBaseConnection();
	Logger logger = Logger.getLogger(EmployeeDaoImpl.class.getName());

	@Override
	public boolean movePaySlips(String employeeId) {

		// initialising the PaySlipDaoImplementation to call methods inside it
		PaySlipDaoImpl paySlipDaoImpl = new PaySlipDaoImpl();

		try (Connection connection = dataBaseConnection.mySqlConnection();
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
			logger.log(Level.SEVERE, "An Error Occurred While Moving Payslips. " + e);
			throw new RuntimeException(e);
		}
		return false;
	}
}
