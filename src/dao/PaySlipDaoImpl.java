package dao;

import model.Payslip;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PaySlipDaoImpl implements PaySlipDao {

	// Query to insert the payslip details into Payslip table
	private static final String INSERT_PAY_SLIP_QUERY = "insert into payslips (employee_id, gross_salary, "
			+ "deductions, net_salary, date_generated) values (?, ?, ?, ?, ?)";
	// Query to retrieve the data from payslips table
	private static final String RETRIEVE_PAY_SLIP_QUERY = "select * from payslips where employee_id = ?";
	// Query for Deleting the Set of records when they match with passed employee id
	private static final String DELETE_PAY_SLIPS_QUERY = "delete from payslips where employee_id = ?";
	final EmployeeDaoImpl employeeDaoImpl = new EmployeeDaoImpl();
    Logger logger = Logger.getLogger(EmployeeDaoImpl.class.getName());
	final DataBaseConnection dataBaseConnection = new DataBaseConnection();

	@Override
	// This method is Designed to add the record into a Word file and table
	public boolean generatePaySlip(Payslip payslip) {

		try (Connection connection = dataBaseConnection.mySqlConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PAY_SLIP_QUERY);) {
			// Prepared Statement using Connection and Setting the dynamic values which are
			// received as arguments

			preparedStatement.setString(1, payslip.getEmployeeId());
			preparedStatement.setDouble(2, payslip.getGrossSalary());
			preparedStatement.setDouble(3, payslip.getDeductions());
			preparedStatement.setDouble(4, payslip.getNetSalary());
			preparedStatement.setDate(5, Date.valueOf(payslip.getDateGenerated()));

			int rowsAffected = preparedStatement.executeUpdate();
			// Checking weather the data is inserted or not
			if (rowsAffected > 0) {
				return true;
			}
		} // To Handle Exception raised by database
		catch (SQLException e) {
            logger.log(Level.SEVERE, "An Error Occurred While Generating the Payslip. " + e);
            throw new RuntimeException(e);
		}
		return false;
	}

	@Override
	// This method is Designed to return a single Payslip details
	public Payslip findPaySlip(String employeeId) {

		// Establishing the Connection
		Connection connection = dataBaseConnection.mySqlConnection();
		// Stripping the Strings to remove the spaces around the String
		String tempEmployeeId = employeeId.strip().toLowerCase();
		// Entering Block if EmployeeId is not empty
		if (!tempEmployeeId.isEmpty()) {

			// Prepared Statement using Connection and Setting the dynamic values which are
			// received as arguments
			PreparedStatement pstmtForExistingPaySlip;
			try {
				// Prepared Statement using Connection and Setting the dynamic values which are
				// received as arguments
				pstmtForExistingPaySlip = connection.prepareStatement(RETRIEVE_PAY_SLIP_QUERY);
				pstmtForExistingPaySlip.setString(1, tempEmployeeId.toLowerCase());

				// ResultSet After Execution it will Store the Result
				ResultSet rsForPaySlip = pstmtForExistingPaySlip.executeQuery();

				// Returning an Anonymous PaySlips Object if PaySlip Exists in a table
				if (rsForPaySlip.next()) {
					return new Payslip(rsForPaySlip.getInt("paySlip_id"), rsForPaySlip.getString("employee_id"),
							rsForPaySlip.getDouble("gross_salary"), rsForPaySlip.getDouble("deductions"),
							rsForPaySlip.getDouble("net_salary"), rsForPaySlip.getDate("date_generated").toLocalDate());
				}
			} catch (SQLException e) {
                logger.log(Level.SEVERE, "An Error Occurred While Fetching the Payslip Information. " + e);
                throw new RuntimeException(e);
			}
		} // Else Block For Empty Data
		else {
			System.out.println();
			System.out.println("Please Enter a Valid Data");
			System.out.println();
		}

		// Returning null if PaySlip not Found for the Employee id in database
		return null;
	}

	@Override
	// This method is designed to delete the Payslip records from database
	public boolean deletePaySlips(String employeeId) {

		// Establishing the Connection

		try (Connection connection = dataBaseConnection.mySqlConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PAY_SLIPS_QUERY);) {
			// Prepared Statement using Connection and Setting the dynamic values which are
			// received as arguments
			preparedStatement.setString(1, employeeId);
			// Executing the Query
			preparedStatement.executeUpdate();
			return true;

		} catch (SQLException e) {
            logger.log(Level.SEVERE, "An Error Occurred While Deleting the PaySlip Information. " + e);
            throw new RuntimeException(e);
		}
	}
}
