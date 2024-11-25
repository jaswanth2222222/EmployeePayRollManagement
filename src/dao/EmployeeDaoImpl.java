package dao;

//imports that are used in the code

import model.Employee;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

//EmployeeDaoImplementation implements EmployeeDao to utilize and implement the abstract
// methods in the EmployeeDao interface
public class EmployeeDaoImpl implements EmployeeDao {

    // Query to Update the Department for the Given Employee id
    private static final String UPDATE_DEPT_BY_EMPLOYEE_ID = "update employees set department = ? where employee_id = ?";
    // Query for retrieving the Department to check weather department is present or
    // not
    private static final String RETRIEVE_DEPARTMENT_BY_NAME_QUERY = "select * from departments where name like ?";
    // Query to Update the Department for the Given Employee id
    private static final String UPDATE_DEPT_AND_SALARY_BY_EMPLOYEE_ID = "update employees set department = ?, "
            + "salary = ? where employee_id = ?";
    // Query for inserting the Employee Details
    private static final String INSERT_EMPLOYEE_QUERY = "insert into employees (employee_id, name, "
            + "department, salary) values (?, ?, ?, ?)";
    // Query for retrieving the Employee to check the uniqueness of employee id
    private static final String RETRIEVE_EMPLOYEE_BY_ID_QUERY = "select * from employees where employee_id like ?";
    // Query to retrieve Employee Details Based on Department name
    private static final String FETCH_EMPLOYEES_BY_DEPT_NAME = "select employee_id, name, salary from employees "
            + "where department = ?";
    // Query to get the count of Employees in a Department by using aggregate
    // Function count()
    private static final String COUNT_OF_EMPLOYEES_BY_DEPT_NAME = "select count(employee_id) as count "
            + "from employees where department = ?";
    // Query to get the Average salary of Employees in a Department by using
    // aggregate Function avg()
    private static final String AVG_SALARY_OF_EMPLOYEES_BY_DEPT_NAME = "select avg(salary) as average from "
            + "employees where department = ?";
    // Query to Update the Department for the Given Employee id
    private static final String UPDATE_SALARY_BY_EMPLOYEE_ID = "update employees set salary = ? where employee_id = ?";
    // Query Syntax for deleting the Employee from Employees
    private static final String DELETE_EMPLOYEE_BY_ID_QUERY = "delete from employees where employee_id = ?";
    // Query to Update the Employee Details where Employee id matches the passed
    // Employee id
    private static final String UPDATE_EMPLOYEE_QUERY = "update employees set name = ?, department = ?, "
            + "salary = ? where employee_id = ?";
    // initializing the Connection Class to call getConnection() Method it can be
    // used in any of the method
    final DataBaseConnection dataBaseConnection = new DataBaseConnection();
    Logger logger = Logger.getLogger(EmployeeDaoImpl.class.getName());

    public boolean isDepartmentExists(String departmentName) {

        // Got Connection
        // Prepared Statement using Connection and Setting the dynamic values which are
        // received as arguments
        try (Connection connection = dataBaseConnection.mySqlConnection();
             PreparedStatement pstmtForExistingDepartment = connection
                     .prepareStatement(RETRIEVE_DEPARTMENT_BY_NAME_QUERY)) {
            pstmtForExistingDepartment.setString(1, departmentName);
            // ResultSet for storing Department Based on Name
            ResultSet resultSetForDepartment = pstmtForExistingDepartment.executeQuery();

            boolean departmentExist = resultSetForDepartment.next();
            return departmentExist;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An Error Occurred While Fetching the Department Information. " + e);
            throw new RuntimeException(e);
        }
    }

    public boolean isEmployeeExists(String employeeId) {
        // Got Connection

        // Prepared Statement using Connection and Setting the dynamic values which are
        // received as arguments
        ResultSet rsForExistingEmployee = null;
        try (Connection connection = dataBaseConnection.mySqlConnection();
             PreparedStatement pstmtForExistingEmployee = connection
                     .prepareStatement(RETRIEVE_EMPLOYEE_BY_ID_QUERY)) {
            pstmtForExistingEmployee.setString(1, employeeId);
            // ResultSet for storing Employee Based on id
            rsForExistingEmployee = pstmtForExistingEmployee.executeQuery();
            // .next() returns a boolean value if the data is present in table's next row
            return rsForExistingEmployee.next();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An Error Occurred While Fetching the Employee Information. " + e);
            throw new RuntimeException(e);
        }
    }

    @Override
    // This method is designed to add the Employee Details to the Database
    public boolean addEmployee(Employee employee) {

        // Got Connection
        try (Connection connection = dataBaseConnection.mySqlConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_EMPLOYEE_QUERY);) {
            // Prepared Statement using Connection and Setting the dynamic values which are
            // received as arguments

            FileHandler fileHandler = new FileHandler("logFile.log", true);
            preparedStatement.setString(1, employee.getEmployeeId());
            preparedStatement.setString(2, employee.getName());
            preparedStatement.setString(3, employee.getDepartment());
            preparedStatement.setDouble(4, employee.getSalary());

            int rowsAffected = preparedStatement.executeUpdate();
            return true;
        } catch (SQLException | IOException e) {
            logger.log(Level.SEVERE, "An Error Occurred While Adding Employee Information. " + e);
            throw new RuntimeException(e);
        }
    }

    // This method is designed to retrieve the Employee Details Based on their
    // Employee id
    // This method is designed to return the Employees Object if passed id matches
    // with the employeeId in the Database
    @Override
    public Employee findEmployeeById(String employeeId) {

        // getting connection for the database

        try (Connection connection = dataBaseConnection.mySqlConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(RETRIEVE_EMPLOYEE_BY_ID_QUERY)) {
            // Prepared Statement using Connection and Setting the dynamic values which are
            // received as arguments
            preparedStatement.setString(1, employeeId);

            // ResultSet for storing Employee Based on id
            ResultSet resultSet = preparedStatement.executeQuery();

            // .next() returns a boolean value if the data is present in table's next row
            // If the data is present then this will return an Anonymous Employees Object
            if (resultSet.next()) {
                Employee foundEmployee = new Employee(resultSet.getString("employee_id"),
                        resultSet.getString("name"),
                        resultSet.getString("department"),
                        resultSet.getDouble("salary"));

                return foundEmployee;
            }
        } // Catch Block to handle Exceptions in Runtime
        catch (SQLException e) {
            logger.log(Level.SEVERE, "An Error Occurred While Fetching the Employee Information. " + e);
            throw new RuntimeException(e);
        }
        return null;
    }

    public int getCountOfEmployeesInDept(String departmentName) {

        // Got Connection
        try (Connection connection = dataBaseConnection.mySqlConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(COUNT_OF_EMPLOYEES_BY_DEPT_NAME)) {
            // Prepared Statement using Connection and Setting the dynamic values which are
            // received as arguments

            preparedStatement.setString(1, departmentName);
            // ResultSet for storing count of Employees Based on department
            ResultSet resultSetForCount = preparedStatement.executeQuery();
            int countOfEmployees = 0;
            if (resultSetForCount.next())
                // retrieved the count and stored in a variable
                countOfEmployees = resultSetForCount.getInt("count");
            return countOfEmployees;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An Error Occurred While Counting the Employees. " + e);
            throw new RuntimeException(e);
        }
    }

    public double getAverageSalaryOfDepartment(String departmentName) {

        // Got Connection
        try (Connection connection = dataBaseConnection.mySqlConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement(AVG_SALARY_OF_EMPLOYEES_BY_DEPT_NAME)) {
            // Prepared Statement using Connection and Setting the dynamic values which are
            // received as arguments

            preparedStatement.setString(1, departmentName);
            // ResultSet for storing Employees Average Salary
            ResultSet resultSetForAverageSalary = preparedStatement.executeQuery();
            resultSetForAverageSalary.next();

            return resultSetForAverageSalary.getDouble("average");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An Error Occurred While Calculating Average Salary of Employee. " + e);
            throw new RuntimeException(e);
        }

    }

    public ArrayList<Employee> getAllEmployeesByDeptName(String departmentName) {

        // Got Connection
        try (Connection connection = dataBaseConnection.mySqlConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FETCH_EMPLOYEES_BY_DEPT_NAME)) {
            // Prepared Statement using Connection and Setting the dynamic values which are
            // received as arguments
            preparedStatement.setString(1, departmentName);
            // ResultSet for storing Employee Based on department
            ResultSet resultSetForEmployeeList = preparedStatement.executeQuery();
            ArrayList<Employee> listOfEmployees = new ArrayList();

            while (resultSetForEmployeeList.next()) {
                Employee employee = new Employee();
                employee.setEmployeeId(resultSetForEmployeeList.getString("employee_id"));
                employee.setName(resultSetForEmployeeList.getString("name"));
                employee.setDepartment(resultSetForEmployeeList.getString("name"));
                employee.setSalary(resultSetForEmployeeList.getDouble("salary"));

                listOfEmployees.add(employee);
            }
            return listOfEmployees;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An Error Occurred While Fetching the Employees List. " + e);
            throw new RuntimeException(e);
        }
    }

    @Override
    // This method is designed to only Promote the Employees and not designed to for
    // salary increment
    public boolean promoteEmployee(String employeeId, String employeeDepartment) {

        // Got Connection
        try (Connection connection = dataBaseConnection.mySqlConnection();
             PreparedStatement pstmtForUpdateDeptByEmployeeId = connection
                     .prepareStatement(UPDATE_DEPT_BY_EMPLOYEE_ID)) {
            // Prepared Statement using Connection and Setting the dynamic values which are
            // received as arguments
            pstmtForUpdateDeptByEmployeeId.setString(1, employeeDepartment);
            pstmtForUpdateDeptByEmployeeId.setString(2, employeeId);

            // Storing the Number of Rows got Affected after executing the Query
            int rowsAffectedForUpdatedEmployee = pstmtForUpdateDeptByEmployeeId.executeUpdate();

            // Success block if Query gets executed without any errors or exceptions
            if (rowsAffectedForUpdatedEmployee > 0)
                return true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An Error Occurred While Promoting the Employee. " + e);
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    // This method is designed to only Increment Salary of the Employees and not to
    // promote the employees
    public boolean incrementEmployee(String employeeId, double salary) {

        // Got Connection

        try (Connection connection = dataBaseConnection.mySqlConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SALARY_BY_EMPLOYEE_ID)) {
            // Prepared Statement using Connection and Setting the dynamic values which are
            // received as arguments
            preparedStatement.setString(2, employeeId);
            preparedStatement.setDouble(1, salary);

            // Storing the boolean value which refers to the data in ResultSet
            int rowsAffectedForUpdatedEmployee = preparedStatement.executeUpdate();

            // Success Block if Query gets Executed
            if (rowsAffectedForUpdatedEmployee > 0) {
                return true;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An Error Occurred While Incrementing the Employee. " + e);
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    // This method is designed to Promote and Increment the Salary of Employees
    public boolean promoteAndIncrementEmployee(String employeeId, String departmentName, double salary) {

        // Got Connection

        try (Connection connection = dataBaseConnection.mySqlConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement(UPDATE_DEPT_AND_SALARY_BY_EMPLOYEE_ID)) {
            // Prepared Statement using Connection and Setting the dynamic values which are
            // received as arguments

            preparedStatement.setString(1, departmentName);
            preparedStatement.setString(3, employeeId);
            preparedStatement.setDouble(2, salary);

            // Storing number of rows affected when query is executed
            int rowsAffectedForUpdatedEmployee = preparedStatement.executeUpdate();

            // Checking Weather the record is updated or not
            if (rowsAffectedForUpdatedEmployee > 0)
                return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    // This method is designed to Delete the Employees whose Payslips are settled
    public boolean deleteEmployee(String employeeId) {

        Connection connection = dataBaseConnection.mySqlConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_EMPLOYEE_BY_ID_QUERY);
            preparedStatement.setString(1, "employeeId");

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An Error Occurred While Removing the Employee Information. " + e);
            throw new RuntimeException(e);
        }

    }

    @Override
    // This method is designed to update the Existing Employee Details
    public boolean updateEmployeeById(Employee employee) {

        // Got Connection
        try (Connection connection = dataBaseConnection.mySqlConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_EMPLOYEE_QUERY)) {
            // Prepared Statement using Connection and Setting the dynamic values which are
            // received as arguments
            preparedStatement.setString(4, employee.getEmployeeId());
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getDepartment());
            preparedStatement.setDouble(3, employee.getSalary());

            // initialising the number of rows Affected after executing the Query
            preparedStatement.executeUpdate();

            // Checking weather the data is inserted or not
            return true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An Error Occurred While Updating the Employee Information. " + e);
            throw new RuntimeException(e);
        }
    }

}
