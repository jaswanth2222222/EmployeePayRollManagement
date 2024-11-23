package dao;

import model.Employee;
import service.PaySlipService;
import model.Payslip;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;

public class PaySlipDaoImpl implements PaySlipDao {

    //Query to insert the payslip details into Payslip table
    private static final String INSERT_PAY_SLIP_QUERY = "insert into payslips (employee_id, gross_salary, " +
            "deductions, net_salary, date_generated) values (?, ?, ?, ?, ?)";
    //Query to retrieve the data from payslips table
    private static final String RETRIEVE_PAY_SLIP_QUERY = "select * from payslips where employee_id = ?";
    //Query for Deleting the Set of records when they match with passed employee id
    private static final String DELETE_PAY_SLIPS_QUERY = "delete from payslips where employee_id = ?";
    final EmployeeDaoImpl employeeDaoImpl = new EmployeeDaoImpl();
    final DataBaseConnection dataBaseConnection = new DataBaseConnection();


    @Override
    //This method is Designed to add the record into a Word file and table
    public void generatePaySlip(String employeeId) {

        String tempEmployeeId = employeeId.strip().toLowerCase();

        //Establishing the Connection
        Connection connection = dataBaseConnection.getDataBaseConnection();

        //Calling and Storing the Returned Employee Object
        Employee employee = employeeDaoImpl.findEmployeeById(tempEmployeeId);
        double grossSalary;

        //Checking Weather the returned Object is null, or it contains any data
        if (employee != null) {

            PaySlipService paySlipService = new PaySlipService();

            //Defining the Gross Salary of an Employee
            grossSalary = employee.getSalary();
            //Calculating the Total Deductions by calling calculateTax(salary)
            double tax = paySlipService.calculateTax(grossSalary);
            double pfDeductions = 0.12 * grossSalary;
            double totalDeductions = tax + pfDeductions;
            //calculating net salary
            double netSalary = grossSalary - totalDeductions;

            try {
                //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PAY_SLIP_QUERY);
                preparedStatement.setString(1, employee.getEmployeeId().toLowerCase());
                preparedStatement.setDouble(2, grossSalary);
                preparedStatement.setDouble(3, totalDeductions);
                preparedStatement.setDouble(4, netSalary);
                preparedStatement.setDate(5, Date.valueOf(LocalDate.now()));

                int rowsAffected = preparedStatement.executeUpdate();
                //Checking weather the data is inserted or not
                if (rowsAffected > 0) {
                    System.out.println();
                    System.out.println("PaySlip Generated for Employee Id : " + employee.getEmployeeId());
                    System.out.println();
                    connection.close();
                }
                HashMap<String, Double> financeMap = new HashMap<>();
                financeMap.put("grossSalary", grossSalary);
                financeMap.put("pfDeductions", pfDeductions);
                financeMap.put("tax", tax);
                financeMap.put("netSalary", netSalary);
                paySlipService.generatePaySlipFile(employee, financeMap);

            }// To Handle Exception raised by database
            catch (SQLException e) {
                System.out.println("An Error Occurred while Generating the Payslip" + e.getMessage());
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("An Error Occurred while Closing the Connection" + e.getMessage());
                }
            }
        } else {
            System.out.println();
            System.out.println("Employee Not Found Please try Again");
            System.out.println();
        }

    }

    @Override
    //This method is Designed to return a single Payslip details
    public Payslip findPaySlip(String employeeId) {

        //Establishing the Connection
        Connection connection = dataBaseConnection.getDataBaseConnection();
        //Stripping the Strings to remove the spaces around the String
        String tempEmployeeId = employeeId.strip().toLowerCase();
        //Entering Block if EmployeeId is not empty
        if (!tempEmployeeId.isEmpty()) {

            //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
            PreparedStatement pstmtForExistingPaySlip;
            try {
                //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
                pstmtForExistingPaySlip = connection.prepareStatement(RETRIEVE_PAY_SLIP_QUERY);
                pstmtForExistingPaySlip.setString(1, tempEmployeeId.toLowerCase());

                //ResultSet After Execution it will Store the Result
                ResultSet rsForPaySlip = pstmtForExistingPaySlip.executeQuery();

                //Returning an Anonymous PaySlips Object if PaySlip Exists in a table
                if (rsForPaySlip.next()) {
                    return new Payslip(rsForPaySlip.getInt("paySlip_id"),
                            rsForPaySlip.getString("employee_id"),
                            rsForPaySlip.getDouble("gross_salary"),
                            rsForPaySlip.getDouble("deductions"),
                            rsForPaySlip.getDouble("net_salary"),
                            rsForPaySlip.getDate("date_generated").toLocalDate());
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("An Error Occurred while Closing the Connection" + e.getMessage());
                }
            }
        } //Else Block For Empty Data
        else {
            System.out.println();
            System.out.println("Please Enter a Valid Data");
            System.out.println();
        }

        //Returning null if PaySlip not Found for the Employee id in database
        return null;
    }

    @Override
    //This method is designed to delete the Payslip records from database
    public void deletePaySlips(String employeeId) {

        //Establishing the Connection
        Connection connection = dataBaseConnection.getDataBaseConnection();
        try {
            //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
            PreparedStatement pstmtForDeletePaySlips = connection.prepareStatement(DELETE_PAY_SLIPS_QUERY);
            pstmtForDeletePaySlips.setString(1, employeeId);
            //Executing the Query
            pstmtForDeletePaySlips.executeUpdate();
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("An Error Occurred while Closing the Connection" + e.getMessage());
            }
        }
    }
}
