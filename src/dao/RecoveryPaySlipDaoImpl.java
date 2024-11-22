package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RecoveryPaySlipDaoImpl implements RecoveryPaySlipDao {

    //Query Syntax for Copying the data from table to table
    private static final String COPY_PAYSLIPS_QUERY = "insert into recovery_payslips select * from payslips " +
            "where employee_id = ?";
    //initialising the Connection Class to call getConnection() Method it can be used in any of the method
    DataBaseConnection dataBaseConnection = new DataBaseConnection();
    //Got Connection
    Connection connection = dataBaseConnection.getDataBaseConnection();

    @Override
    public void movePaySlips(String employeeId) {

        //initialising the PaySlipDaoImplementation to call methods inside it
        PaySlipDaoImpl paySlipDaoImpl = new PaySlipDaoImpl();

        try {
            //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
            PreparedStatement pstmtForCopyingPaySlips = connection.prepareStatement(COPY_PAYSLIPS_QUERY);
            pstmtForCopyingPaySlips.setString(1, employeeId);
            //Storing number of rows affected after executing the query
            int rowsInserted = pstmtForCopyingPaySlips.executeUpdate();
            //Checking whether the query is executed properly or not
            if (rowsInserted > 0) {
                //Calling an external method to delete payslips in payslips table
                paySlipDaoImpl.deletePaySlips(employeeId);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
