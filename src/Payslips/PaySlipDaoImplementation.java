package Payslips;

import Connection.DataBaseConnection;
import Employees.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class PaySlipDaoImplementation implements PaySlipDao{

    EmployeeDaoImplementation employeeDaoImplementation = new EmployeeDaoImplementation();
    DataBaseConnection dataBaseConnection = new DataBaseConnection();

    @Override
    //This method is Designed to add the record into a Word file and table
    public void generatePaySlip(String employeeId) {

        //Calling and Storing the Returned Employee Object
        Employees employee = employeeDaoImplementation.returnEmployee(employeeId);
        double grossSalary;
        //initialising the DataVaseConnection class
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        //Query to insert the payslip details into Payslip table
        final String insertPaySlipQuery = "insert into payslips (employee_id, gross_salary, deductions, net_salary, date_generated) values (?, ?, ?, ?, ?)";

        //Checking Weather the returned Object is null, or it contains any data
        if (employee != null) {

            PaySlipService paySlipService = new PaySlipService();

            //Defining the Gross Salary of an Employee
            grossSalary = employee.getSalary();
            //Calculating the Total Deductions by calling calculateTax(salary)
            double tax = paySlipService.calculateTax(grossSalary);
            double pfDeductions = 0.12*grossSalary;
            double totalDeductions = tax + pfDeductions;
            //calculating net salary
            double netSalary = grossSalary - totalDeductions;

            //Establishing the Connection
            Connection connection = dataBaseConnection.getDataBaseConnection();
            try {
                //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
                PreparedStatement preparedStatement = connection.prepareStatement(insertPaySlipQuery);
                preparedStatement.setString(1, employee.getEmployee_id().toLowerCase());
                preparedStatement.setDouble(2, grossSalary);
                preparedStatement.setDouble(3, totalDeductions);
                preparedStatement.setDouble(4, netSalary);
                preparedStatement.setDate(5, Date.valueOf(LocalDate.now()));

                int rowsAffected = preparedStatement.executeUpdate();
                //Checking weather the data is inserted or not
                if (rowsAffected > 0) {
                    System.out.println();
                    System.out.println("PaySlip Generated for Employee Id : " + employee.getEmployee_id());
                    System.out.println();
                }
                //Using System millis to keep the separate file for each one
                String filename = String.valueOf(System.currentTimeMillis()) + ".word";

                //Initialising the BufferedWriter class and opened a Word file named PaySlips.word in append mode
                BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));
                writer.write("\n \n PaySlip for the Employee with Id :" + employee.getEmployee_id());
                writer.write("\n Employee Name : " + employee.getName());
                writer.write("\n Employee Department : " + employee.getDepartment());
                writer.write("\n Employee Gross Salary : " + grossSalary);
                writer.write("\n Employee Total Deduction towards Pf : " + pfDeductions);
                writer.write("\n Employee Total Deduction towards Tax : " + tax);
                writer.write("\n Employee Net or InHand Salary : " + netSalary);
                writer.close();
            }// To Handle Exception raised by database
            catch (SQLException e) {
                throw new RuntimeException(e);
            }//To Handle Exception raised by writer or files
            catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else {
            System.out.println();
            System.out.println("Employee Not Found Please try Again");
            System.out.println();
        }

    }

    @Override
    //This method is Designed to return a single Payslip details
    public Payslips returnPaySlip(String employeeId) {

        //Stripping the Strings to remove the spaces around the String
        employeeId = employeeId.strip();
        //Entering Block if EmployeeId is not empty
        if (!employeeId.isEmpty()) {

            //Establishing the Connection from database
            Connection connection = dataBaseConnection.getDataBaseConnection();

            //Query to retrieve the data from payslips table
            final String retrievePaySlipQuery = "select * from payslips where employee_id = ?";

            //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
            PreparedStatement preparedStatementForExistingPaySlip = null;
            try {
                //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
                preparedStatementForExistingPaySlip = connection.prepareStatement(retrievePaySlipQuery);
                preparedStatementForExistingPaySlip.setString(1, employeeId.toLowerCase());

                //ResultSet After Execution it will Store the Result
                ResultSet resultSetForPaySlip = preparedStatementForExistingPaySlip.executeQuery();

                //Returning an Anonymous PaySlips Object if PaySlip Exists in a table
                if(resultSetForPaySlip.next()){
                    return new Payslips(resultSetForPaySlip.getInt("paySlip_id"),
                            resultSetForPaySlip.getString("employee_id"),
                            resultSetForPaySlip.getDouble("gross_salary"),
                            resultSetForPaySlip.getDouble("deductions"),
                            resultSetForPaySlip.getDouble("net_salary"),
                            resultSetForPaySlip.getDate("date_generated").toLocalDate());
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
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
        //Query for Deleting the Set of records when they match with passed employee id
        final String deletePaySlipsQuery = "delete from payslips where employee_id = ?";

        //Establishing the Connection
        Connection connection = dataBaseConnection.getDataBaseConnection();

        try {
            //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
            PreparedStatement preparedStatementForDeletePaySlips = connection.prepareStatement(deletePaySlipsQuery);
            preparedStatementForDeletePaySlips.setString(1, employeeId);
            //Executing the Query
            preparedStatementForDeletePaySlips.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
