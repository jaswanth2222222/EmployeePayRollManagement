package Employees;

//imports that are used in the code
import Connection.DataBaseConnection;
import Payslips.PaySlipDaoImplementation;
import Payslips.Payslips;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//EmployeeDaoImplementation implements EmployeeDao to utilize and implement the abstract methods in the EmployeeDao interface

public class EmployeeDaoImplementation implements EmployeeDao {

    //initialising the Connection Class to call getConnection() Method it can be used in any of the method
    DataBaseConnection dataBaseConnection = new DataBaseConnection();


    @Override
    //This method is designed to add the Employee Details to the Database
    public void addEmployee(String employeeId, String employeeName, String employeeDepartment, double employeeSalary) {

        //Removing the Spaces around the String by keeping spaces unchanged between two words
        employeeId = employeeId.strip();
        employeeName = employeeName.strip();
        employeeDepartment = employeeDepartment.strip();

        //conditions to check or validate all the data is passed correctly or not
        if (!employeeId.isEmpty() && employeeName.length()>1 && !employeeDepartment.isEmpty() && employeeSalary > 0) {

            //Query for inserting the Employee Details
            final String insertEmployeeQuery = "insert into employees (employee_id, name, department, salary) values (?, ?, ?, ?)";
            //Query for retrieving the Employee to check the uniqueness of employee id
            final String retrieveEmployeeByIdQuery = "select * from employees where employee_id like ?";
            //Query for retrieving the Department to check weather dept iis present or not
            final String retrieveDepartmentByNameQuery = "select * from departments where name like ?";
            //Got Connection
            Connection connection = dataBaseConnection.getDataBaseConnection();

            //Try-catch block to handle the Exceptions while Raised on Runtime
            try {
                //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
                PreparedStatement preparedStatementForExistingEmployee = connection.prepareStatement(retrieveEmployeeByIdQuery);
                preparedStatementForExistingEmployee.setString(1, employeeId.toLowerCase());
                //ResultSet for storing Employee Based on id
                ResultSet resultSetForExistingEmployee = preparedStatementForExistingEmployee.executeQuery();
                //.next() returns a boolean value if the data is present in table's next row
                boolean confirmEmployee = resultSetForExistingEmployee.next();

                //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
                PreparedStatement preparedStatementForExistingDepartment = connection.prepareStatement(retrieveDepartmentByNameQuery);
                preparedStatementForExistingDepartment.setString(1, employeeDepartment.toLowerCase());
                //ResultSet for storing Department Based on Name
                ResultSet resultSetForDepartment = preparedStatementForExistingDepartment.executeQuery();


                boolean confirmDepartment = resultSetForDepartment.next();
                //Checking weather given EmployeeId is unique or not
                //Checking weather given Department exists in department table or not
                if (!confirmEmployee && confirmDepartment) {
                    try {
                        //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
                        PreparedStatement preparedStatementForAddingEmployee = connection.prepareStatement(insertEmployeeQuery);
                        preparedStatementForAddingEmployee.setString(1, employeeId.toLowerCase());
                        preparedStatementForAddingEmployee.setString(2, employeeName);
                        preparedStatementForAddingEmployee.setString(3, employeeDepartment);
                        preparedStatementForAddingEmployee.setDouble(4, employeeSalary);

                        //initialising the number of rows Affected after executing the Query
                        int rowsAffected = preparedStatementForAddingEmployee.executeUpdate();

                        //Checking how many rows are Affected in a row to know whether record is updated or not
                        if (rowsAffected > 0) {
                            System.out.println();
                            System.out.println("Employee Added Successfully");
                            System.out.println();
                            connection.close();
                            resultSetForDepartment.close();
                            resultSetForExistingEmployee.close();
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } //If control comes to else then there is a chance of Duplicate id or no department matched with passed name
                else {
                    System.out.println();
                    System.out.println("Invalid input or Duplicate EmployeeId Please try Again");
                    System.out.println();

                    //Closing the connection and ResultSet to avoid Data loss
                    connection.close();
                    resultSetForDepartment.close();
                    resultSetForExistingEmployee.close();
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println();
            System.out.println("Invalid input or Duplicate EmployeeId Please try Again");
            System.out.println();
        }
    }

    @Override
    //This method is designed to retrieve the Employee Details Based on their Employee id
    public void getEmployeeById(String employeeId) {

        //Calling the returnEmployee() and storing Employees Object
        Employees employee = returnEmployee(employeeId);
        //checking weather the returned Object is null or not
        if (employee == null) {
            //If Null then there is no employee Exists with id
            System.out.println();
            System.out.println("Employee not Found with Id : " + employeeId + " Please try with Another Id");
            System.out.println();
        } //if employee contains data control comes into else block and print all the Details of an Employee
        else{
            System.out.println();
            System.out.println("Employee Id : " + employee.getEmployee_id() +
                    " | Employee Name : " + employee.getName() +
                    " | Employee Department : " + employee.getDepartment() +
                    " | Employee Salary : " + employee.getSalary());
            System.out.println();
        }

    }

    //This method is designed to return the Employees Object if passed id matches wth the employeeId in the Database
    public Employees returnEmployee(String employeeId) {
        employeeId = employeeId.strip();
        //Defining the RetrieveQuery to get the Employee Details from table
        final String retrieveEmployeeByIdQuery = "select * from employees where employee_id = ?";
        employeeId = employeeId.toLowerCase();

        //getting connection for the database
        Connection connection = dataBaseConnection.getDataBaseConnection();
        try {
            //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
            PreparedStatement preparedStatement = connection.prepareStatement(retrieveEmployeeByIdQuery);
            preparedStatement.setString(1, employeeId);

            //ResultSet for storing Employee Based on id
            ResultSet resultSet = preparedStatement.executeQuery();

            //.next() returns a boolean value if the data is present in table's next row
            //If the data is present then this will return an Anonymous Employees Object
            if (resultSet.next()) {
                return new Employees(resultSet.getString("employee_id"),
                        resultSet.getString("name"),
                        resultSet.getString("department"),
                        resultSet.getDouble("salary"));
            } //If there is no data in table then it will return null
            else {
                return null;
            }
        } //Catch Block to handle Exceptions in Runtime
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    //This method is designed to generate all the Employees in the Database belongs to particular Department
    public void generateDepartmentReport(String departmentName) {

        //Converting the received String to lower case
        departmentName = departmentName.toLowerCase();
        //Query to retrieve Employee Details Based on Department name
        String retrieveEmployeesByDepartmentNameQuery = "select employee_id, name, salary from employees where department = ?";
        //Query to get the count of Employees in a Department by using aggregate Function count()
        String countOfEmployeesByDepartmentNameQuery = "select count(employee_id) as count from employees where department = ?";
        //Query to get the Average salary of Employees in a Department by using aggregate Function avg()
        String averageSalaryOfEmployeesByDepartmentNameQuery = "select avg(salary) as average from employees where department = ?";

        Connection connection = dataBaseConnection.getDataBaseConnection();

        try {
            //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
            PreparedStatement preparedStatementForCount = connection.prepareStatement(countOfEmployeesByDepartmentNameQuery);
            preparedStatementForCount.setString(1, departmentName);
            //ResultSet for storing count of Employees Based on department
            ResultSet resultSetForCount = preparedStatementForCount.executeQuery();
            int countOfEmployees = 0;
            if (resultSetForCount.next())
                // retrieved the count and stored in a variable
                countOfEmployees = resultSetForCount.getInt("count");

            //Checking weather the Employees are present in the Particular Department or not
            if(countOfEmployees != 0) {
                //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
                PreparedStatement preparedStatementForEmployeeList = connection.prepareStatement(retrieveEmployeesByDepartmentNameQuery);
                preparedStatementForEmployeeList.setString(1, departmentName);
                //ResultSet for storing Employee Based on department
                ResultSet resultSetForEmployeeList = preparedStatementForEmployeeList.executeQuery();
                //Checking weather the data is present in next row or not
                while (resultSetForEmployeeList.next()) {
                    //Printing the EmployeeDetails iin the console
                    System.out.println("Employee Id : " + resultSetForEmployeeList.getString("employee_id") + " | Employee Name : " + resultSetForEmployeeList.getString("name") + " | Salary : " + resultSetForEmployeeList.getDouble("salary"));
                }
                //Displaying the count of Employees
                System.out.println();
                System.out.println("Total number of Employees : " + countOfEmployees);
                System.out.println();

                //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
                PreparedStatement preparedStatementForAverageSalary = connection.prepareStatement(averageSalaryOfEmployeesByDepartmentNameQuery);
                preparedStatementForAverageSalary.setString(1, departmentName);
                //ResultSet for storing Employees Average Salary
                ResultSet resultSetForAverageSalary = preparedStatementForAverageSalary.executeQuery();
                resultSetForAverageSalary.next();
                //Displaying the Average Salary of Employees in a Department
                System.out.println("Average Salary for the " + departmentName + "Department is : ₹" + resultSetForAverageSalary.getDouble("average"));
                System.out.println();
            }//this will be executed when countOfEmployees is zero
            else {
                System.out.println("No Employees Found in the Specific Department");
            }
        } //Catch block to handle the Exceptions
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    //This method is designed to only Promote the Employees and not designed to for salary increment
    public void promoteEmployee(String employeeId, String employeeDepartment) {

        //Removing the Spaces around the String by keeping spaces unchanged between two words
        employeeId = employeeId.strip();
        employeeDepartment = employeeDepartment.strip();

        if(!employeeId.isEmpty() && !employeeDepartment.isEmpty()) {
        //Getting Connection for the DataBase
        Connection connection = dataBaseConnection.getDataBaseConnection();

        Employees employee = returnEmployee(employeeId);
        //Checking weather given EmployeeId is present or not
        if (employee != null) {
            //Query for retrieving the Department to check weather dept iis present or not
            final String retrieveDepartmentByNameQuery = "select * from departments where name like ?";

            boolean confirmDepartment;
            try {
                //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
                PreparedStatement preparedStatementForExistingDepartment = connection.prepareStatement(retrieveDepartmentByNameQuery);
                preparedStatementForExistingDepartment.setString(1, employeeDepartment.toLowerCase());
                //ResultSet for storing Department Based on Name
                ResultSet resultSetForDepartment = preparedStatementForExistingDepartment.executeQuery();

                //Storing a boolean value which refers to the presence of Data
                confirmDepartment = resultSetForDepartment.next();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            //Checking weather given Department exist in department table or not
            if (confirmDepartment) {
                //Query to Update the Department for the Given Employee id
                final String updateDepartmentByEmployeeIdQuery = "update employees set department = ? where employee_id = ?";

                try {
                    //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
                    PreparedStatement preparedStatementForUpdateDepartmentByEmployeeId = connection.prepareStatement(updateDepartmentByEmployeeIdQuery);
                    preparedStatementForUpdateDepartmentByEmployeeId.setString(1, employeeDepartment.toLowerCase());
                    preparedStatementForUpdateDepartmentByEmployeeId.setString(2, employeeId.toLowerCase());

                    //Storing the Number of Rows got Affected after executing the Query
                    int rowsAffectedForUpdatedEmployee = preparedStatementForUpdateDepartmentByEmployeeId.executeUpdate();

                    //Success block if Query gets executed without any errors or exceptions
                    if (rowsAffectedForUpdatedEmployee>0) {
                        System.out.println();
                        System.out.println("Employee Promoted Successfully");
                        System.out.println();
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } //Else block if provided Department is not Valid.
            else {
                System.out.println();
                System.out.println("Invalid Department, Please Try Again");
                System.out.println();
            }// Else block if provided Employee id is not Valid.
        } else {
            System.out.println();
            System.out.println("Employee Not found with Id : " + employeeId + " please try again");
            System.out.println();
        }
        } //Else Block For Empty Data
        else {
            System.out.println();
            System.out.println("Please Enter a Valid Data");
            System.out.println();
        }
    }

    @Override
    //This method is designed to only Increment Salary of the Employees and not to promote the employees
    public void incrementEmployee(String employeeId, double salary) {

        //Removing the Spaces around the String by keeping spaces unchanged between two words
        employeeId = employeeId.strip();

        if (!employeeId.isEmpty()) {
        //Getting Connection for the DataBase
        Connection connection = dataBaseConnection.getDataBaseConnection();

        Employees employee = returnEmployee(employeeId);
        //Checking weather given EmployeeId is present or not
        if (employee != null) {

                //Checking weather provided salary is higher than current salary or not
                if(employee.getSalary() < salary) {

                    //Query to Update the Department for the Given Employee id
                    final String updateSalaryByEmployeeIdQuery = "update employees set salary = ? where employee_id = ?";

                    try {
                        //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
                        PreparedStatement preparedStatementForUpdateSalaryByEmployeeId = connection.prepareStatement(updateSalaryByEmployeeIdQuery);
                        preparedStatementForUpdateSalaryByEmployeeId.setString(2, employeeId.toLowerCase());
                        preparedStatementForUpdateSalaryByEmployeeId.setDouble(1, salary);

                        //Storing the boolean value which refers to the data in ResultSet
                        int rowsAffectedForUpdatedEmployee = preparedStatementForUpdateSalaryByEmployeeId.executeUpdate();

                        //Success Block if Query gets Executed
                        if (rowsAffectedForUpdatedEmployee>0) {
                            System.out.println();
                            System.out.println("Employee Salary has been Incremented Successfully");
                            System.out.println();
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } //Else block if provided Salary is less than current one
                else {
                    System.out.println("Invalid Salary, Please provide a higher salary than the current salary (" + employee.getSalary() + ").");
                }
            } //Else block if provided Employee id is not Valid.
        else {
            System.out.println("Employee Not found with Id : " + employeeId + " please try again");
        }
        } //Else Block For Empty Data
        else {
            System.out.println();
            System.out.println("Please Enter a Valid Data");
            System.out.println();
        }
    }

    @Override
    //This method is designed to Promote and Increment the Salary of Employees
    public void promoteAndIncrementEmployee(String employeeId, String employeeDepartment, double salary) {

        //Removing the Spaces around the String by keeping spaces unchanged between two words
        employeeId = employeeId.strip();
        employeeDepartment = employeeDepartment.strip();

        if (!employeeId.isEmpty() && !employeeDepartment.isEmpty()) {
            //Getting Connection for the DataBase
            Connection connection = dataBaseConnection.getDataBaseConnection();

            Employees employee = returnEmployee(employeeId);
            //Checking weather given EmployeeId is present or not
            if (employee != null) {
                //Query for retrieving the Department to check weather dept iis present or not
                final String retrieveDepartmentByNameQuery = "select * from departments where name like ?";

                boolean confirmDepartment;
                try {
                    //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
                    PreparedStatement preparedStatementForExistingDepartment = connection.prepareStatement(retrieveDepartmentByNameQuery);
                    preparedStatementForExistingDepartment.setString(1, employeeDepartment.toLowerCase());
                    //ResultSet for storing Department Based on Name
                    ResultSet resultSetForDepartment = preparedStatementForExistingDepartment.executeQuery();

                    //Storing a boolean value which refers to the data in a ResultSet
                    confirmDepartment = resultSetForDepartment.next();


                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                //Checking weather given Department exist in department table or not
                if (confirmDepartment) {
                    //Checking weather provided salary is higher than current salary or not
                    if (employee.getSalary() < salary) {

                        //Query to Update the Department for the Given Employee id
                        final String updateDepartmentAndSalaryByEmployeeIdQuery = "update employees set department = ?, salary = ? where employee_id = ?";

                        try {
                            //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
                            PreparedStatement preparedStatementForUpdateDepartmentByEmployeeId = connection.prepareStatement(updateDepartmentAndSalaryByEmployeeIdQuery);
                            preparedStatementForUpdateDepartmentByEmployeeId.setString(1, employeeDepartment.toLowerCase());
                            preparedStatementForUpdateDepartmentByEmployeeId.setString(3, employeeId.toLowerCase());
                            preparedStatementForUpdateDepartmentByEmployeeId.setDouble(2, salary);

                            //Storing number of rows affected when query is executed
                            int rowsAffectedForUpdatedEmployee = preparedStatementForUpdateDepartmentByEmployeeId.executeUpdate();

                            //Checking Weather the record is updated or not
                            if (rowsAffectedForUpdatedEmployee > 0) {
                                System.out.println();
                                System.out.println("Employee Promoted and Incremented Successfully");
                                System.out.println();
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                    }//Else block if provided Salary is less than current one
                    else {
                        System.out.println();
                        System.out.println("Invalid Salary, Please provide a higher salary than the current salary (" + employee.getSalary() + ").");
                        System.out.println();
                    }
                }//  Else block if provided Department is not Valid.
                else {
                    System.out.println();
                    System.out.println("Invalid Department, Please Try Again");
                    System.out.println();
                }
            } // Else block if provided Employee id is not Valid.
            else {
                System.out.println();
                System.out.println("Employee Not found with Id : " + employeeId + " please try again");
                System.out.println();
            }
        } //Else Block For Empty Data
        else {
            System.out.println();
            System.out.println("Please Enter a Valid Data");
            System.out.println();
        }
    }

    @Override
    //This method is designed to Delete the Employees whose Payslips are settled
    public void deleteEmployee(String employeeId) {
        //Removing the Spaces around the String by keeping spaces unchanged between two words
        employeeId = employeeId.strip();

        //Checking whether the provided employeeId is empty or it contains any value
        if (!employeeId.isEmpty()) {

            Employees employee = returnEmployee(employeeId);
            if (employee != null) {
                //initialising the PaySlipDaoImplementation to call methods inside it
                PaySlipDaoImplementation paySlipDaoImplementation = new PaySlipDaoImplementation();

                Payslips payslip = paySlipDaoImplementation.returnPaySlip(employeeId);
                if (payslip != null) {
                    Connection connection = dataBaseConnection.getDataBaseConnection();

                    //Query Syntax for Copying the data from table to table
                    final String copyPayslipsQuery = "insert into recoveryPaySlips select * from payslips where employee_id = ?";
                    //Query Syntax for deleting the Employee from Employees
                    final String deleteEmployeeQuery = "delete from employees where employee_id = ?";

                    try {
                        //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
                        PreparedStatement preparedStatementForCopyingPaySlips = connection.prepareStatement(copyPayslipsQuery);
                        preparedStatementForCopyingPaySlips.setString(1, employeeId.toLowerCase());

                        //Storing number of rows affected after executing the query
                        int rowsInserted = preparedStatementForCopyingPaySlips.executeUpdate();

                        //Checking whether the query is executed properly or not
                        if (rowsInserted>0) {
                            //Calling an external method to delete payslips in payslips table
                            paySlipDaoImplementation.deletePaySlips(employeeId.toLowerCase());
                        }

                        //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
                        PreparedStatement preparedStatementForDeletingEmployee = connection.prepareStatement(deleteEmployeeQuery);
                        preparedStatementForDeletingEmployee.setString(1, employeeId.toLowerCase());
                        int rowsDeleted = preparedStatementForDeletingEmployee.executeUpdate();

                        //Success Block if query gets Executed Properly
                        if (rowsDeleted>0) {
                            System.out.println();
                            System.out.println("Employee Removed Successfully");
                            System.out.println();
                        }

                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                } //Else block to settle the PaySlips
                else {
                    System.out.println();
                    System.out.println("Cannot Remove Employee due to Pending Operations");
                    System.out.println();
                }

            } // Else Block for Not found Empployee
            else {
                System.out.println();
                System.out.println("Employee Not Found, Please try Again");
                System.out.println();
            }

        } //Else Block For Empty Data
        else {
            System.out.println();
            System.out.println("Please Enter a Valid Data");
            System.out.println();
        }
    }

    @Override
    //This method is designed to update the Existing Employee Details
    public void updateEmployeeById(String employeeId, String employeeName, String employeeDepartment, double employeeSalary) {

        //Removing the Spaces around the String by keeping spaces unchanged between two words
        employeeId = employeeId.strip();
        employeeName = employeeName.strip();
        employeeDepartment = employeeDepartment.strip();

        //conditions to check or validate all the data is passed correctly or not
        if (!employeeId.isEmpty() && employeeName.length()>1 && !employeeDepartment.isEmpty() && employeeSalary > 0) {

            //Query to Update the Employee Details where Employee id matches the passed Employee id
            final String updateEmployeeQuery = "update employees set name = ?, department = ?, salary = ? where employee_id = ?";
            //Query to retrieve the Employee Details based on the employee id
            final String retrieveEmployeeByIdQuery = "select * from employees where employee_id like ?";
            //Query to retrieve Department based on department name
            final String retrieveDepartmentByNameQuery = "select * from departments where name like ?";
            //Got connection from DataBaseConnection class
            Connection connection = dataBaseConnection.getDataBaseConnection();
            try {
                //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
                PreparedStatement preparedStatementForExistingEmployee = connection.prepareStatement(retrieveEmployeeByIdQuery);
                preparedStatementForExistingEmployee.setString(1, employeeId.toLowerCase());
                //ResultSet for storing Employee Based on id
                ResultSet resultSetForExistingEmployee = preparedStatementForExistingEmployee.executeQuery();

                //Confirming weather the Employee is present or not with respect to the employee id
                boolean confirmEmployee = resultSetForExistingEmployee.next();

                //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
                PreparedStatement preparedStatementForExistingDepartment = connection.prepareStatement(retrieveDepartmentByNameQuery);
                preparedStatementForExistingDepartment.setString(1, employeeDepartment.toLowerCase());
                //ResultSet for storing Department Based on department name
                ResultSet resultSetForDepartment = preparedStatementForExistingDepartment.executeQuery();

                boolean confirmDepartment = resultSetForDepartment.next();

                //Checking weather the given employee id and department exists in the database or not
                if (confirmEmployee && confirmDepartment) {
                    try {
                        //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
                        PreparedStatement preparedStatementForUpdatingEmployee = connection.prepareStatement(updateEmployeeQuery);
                        preparedStatementForUpdatingEmployee.setString(4, employeeId.toLowerCase());
                        preparedStatementForUpdatingEmployee.setString(1, employeeName);
                        preparedStatementForUpdatingEmployee.setString(2, employeeDepartment);
                        preparedStatementForUpdatingEmployee.setDouble(3, employeeSalary);


                        //initialising the number of rows Affected after executing the Query
                        int rowsAffected = preparedStatementForUpdatingEmployee.executeUpdate();

                        //Checking weather the data is inserted or not
                        if (rowsAffected > 0) {
                            System.out.println();
                            System.out.println("Employee Details Updated Successfully");
                            System.out.println();
                            //Closing the resources
                            connection.close();
                            resultSetForDepartment.close();
                            resultSetForExistingEmployee.close();
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                } //Else Block when Employee is not present in database
                else {
                    System.out.println();
                    System.out.println("Invalid input or EmployeeId Not found Please try Again");
                    System.out.println();
                    //Closing the Connections
                    connection.close();
                    resultSetForDepartment.close();
                    resultSetForExistingEmployee.close();
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println();
            System.out.println("Invalid input or EmployeeId Not found Please try Again");
            System.out.println();
        }


    }

}
