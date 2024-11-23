package dao;

//imports that are used in the code

import model.Employee;
import model.Payslip;
import service.ValidationService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//EmployeeDaoImplementation implements EmployeeDao to utilize and implement the abstract
// methods in the EmployeeDao interface
public class EmployeeDaoImpl implements EmployeeDao {

    //Query to Update the Department for the Given Employee id
    private static final String UPDATE_DEPT_BY_EMPLOYEE_ID = "update employees set department = ? where employee_id = ?";
    //Query for retrieving the Department to check weather dept iis present or not
    private static final String RETRIEVE_DEPARTMENT_BY_NAME_QUERY = "select * from departments where name like ?";

    //Query to Update the Department for the Given Employee id
    private static final String UPDATE_DEPT_AND_SALARY_BY_EMPLOYEE_ID = "update employees set department = ?, " +
            "salary = ? where employee_id = ?";

    //Query for inserting the Employee Details
    private static final String INSERT_EMPLOYEE_QUERY = "insert into employees (employee_id, name, " +
            "department, salary) values (?, ?, ?, ?)";
    //Query for retrieving the Employee to check the uniqueness of employee id
    private static final String RETRIEVE_EMPLOYEE_BY_ID_QUERY = "select * from employees where employee_id like ?";

    //Query to retrieve Employee Details Based on Department name
    private static final String FETCH_EMPLOYEES_BY_DEPT_NAME = "select employee_id, name, salary from employees " +
            "where department = ?";
    //Query to get the count of Employees in a Department by using aggregate Function count()
    private static final String COUNT_OF_EMPLOYEES_BY_DEPT_NAME = "select count(employee_id) as count " +
            "from employees where department = ?";
    //Query to get the Average salary of Employees in a Department by using aggregate Function avg()
    private static final String AVG_SALARY_OF_EMPLOYEES_BY_DEPT_NAME = "select avg(salary) as average from " +
            "employees where department = ?";
    //Query to Update the Department for the Given Employee id
    private static final String UPDATE_SALARY_BY_EMPLOYEE_ID = "update employees set salary = ? where employee_id = ?";
    //Query Syntax for deleting the Employee from Employees
    private static final String DELETE_EMPLOYEE_BY_ID_QUERY = "delete from employees where employee_id = ?";
    //Query to Update the Employee Details where Employee id matches the passed Employee id
    private static final String UPDATE_EMPLOYEE_QUERY = "update employees set name = ?, department = ?, " +
            "salary = ? where employee_id = ?";
    //initialising the Connection Class to call getConnection() Method it can be used in any of the method
    final DataBaseConnection dataBaseConnection = new DataBaseConnection();
    //initialising the Connection Class to call getConnection() Method it can be used in any of the method
    final RecoveryPaySlipDaoImpl recoveryPaySlipDaoImpl = new RecoveryPaySlipDaoImpl();
    final ValidationService validationService = new ValidationService();

    public boolean isDepartmentExists(String departmentName) {

        //Got Connection
        Connection connection = dataBaseConnection.getDataBaseConnection();
        //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
        PreparedStatement pstmtForExistingDepartment;
        ResultSet resultSetForDepartment = null;
        try {
            pstmtForExistingDepartment = connection.prepareStatement(RETRIEVE_DEPARTMENT_BY_NAME_QUERY);
            pstmtForExistingDepartment.setString(1, departmentName);
            //ResultSet for storing Department Based on Name
            resultSetForDepartment = pstmtForExistingDepartment.executeQuery();

            boolean departmentExist = resultSetForDepartment.next();
            return departmentExist;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
                resultSetForDepartment.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean isEmployeeExists(String employeeId) {
        //Got Connection
        Connection connection = dataBaseConnection.getDataBaseConnection();

        //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
        ResultSet rsForExistingEmployee = null;
        try {
            PreparedStatement pstmtForExistingEmployee;
            pstmtForExistingEmployee = connection.prepareStatement(RETRIEVE_EMPLOYEE_BY_ID_QUERY);
            pstmtForExistingEmployee.setString(1, employeeId);
            //ResultSet for storing Employee Based on id
            rsForExistingEmployee = pstmtForExistingEmployee.executeQuery();
            //.next() returns a boolean value if the data is present in table's next row
            return rsForExistingEmployee.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
                rsForExistingEmployee.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    //This method is designed to add the Employee Details to the Database
    public boolean addEmployee(Employee employee) {

        //Got Connection
        Connection connection = dataBaseConnection.getDataBaseConnection();

        boolean isDepartmentExists = isDepartmentExists(employee.getDepartment());
        boolean isEmployeeExists = isEmployeeExists(employee.getEmployeeId());

        //Checking weather given EmployeeId is unique or not
        //Checking weather given Department exists in department table or not
        if (isEmployeeExists && isDepartmentExists) {
            try {
                //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
                PreparedStatement pstmtForAddingEmployee = connection.prepareStatement(INSERT_EMPLOYEE_QUERY);
                pstmtForAddingEmployee.setString(1, employee.getEmployeeId());
                pstmtForAddingEmployee.setString(2, employee.getName());
                pstmtForAddingEmployee.setString(3, employee.getDepartment());
                pstmtForAddingEmployee.setDouble(4, employee.getSalary());

                //initialising the number of rows Affected after executing the Query
                int rowsAffected = pstmtForAddingEmployee.executeUpdate();

                //Checking how many rows are Affected in a row to know whether record is updated or not
                if (rowsAffected > 0) {
                    return true;
                }
            } catch (SQLException e) {
                System.out.println("An Error Occurred while inserting Employee " +
                        "Details into Database." + e.getMessage());
                throw new RuntimeException(e);
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }//If control comes to else then there is a chance of Duplicate id
        // or no department matched with passed name
        else {
            return false;
        }
        return false;
    }

    @Override
    //This method is designed to retrieve the Employee Details Based on their Employee id
    public void getEmployeeById(String employeeId) {

        //Calling the returnEmployee() and storing Employees Object
        Employee employee = findEmployeeById(employeeId);
        //checking weather the returned Object is null or not
        if (employee == null) {
            //If Null then there is no employee Exists with id
            System.out.println();
            System.out.println("Employee not Found with Id : " + employeeId + " Please try with Another Id");
            System.out.println();
        } //if employee contains data control comes into else block and print all the Details of an Employee
        else {
            System.out.println();
            System.out.println("Employee Id : " + employee.getEmployeeId() +
                    " | Employee Name : " + employee.getName() +
                    " | Employee Department : " + employee.getDepartment() +
                    " | Employee Salary : " + employee.getSalary());
            System.out.println();
        }

    }

    //This method is designed to return the Employees Object if passed id matches wth the employeeId in the Database
    public Employee findEmployeeById(String employeeId) {

        //getting connection for the database
        Connection connection = dataBaseConnection.getDataBaseConnection();
        try {
            //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
            PreparedStatement preparedStatement = connection.prepareStatement(RETRIEVE_EMPLOYEE_BY_ID_QUERY);
            preparedStatement.setString(1, employeeId);

            //ResultSet for storing Employee Based on id
            ResultSet resultSet = preparedStatement.executeQuery();

            //.next() returns a boolean value if the data is present in table's next row
            //If the data is present then this will return an Anonymous Employees Object
            if (resultSet.next()) {
                Employee foundEmployee = new Employee(resultSet.getString("employee_id"),
                        resultSet.getString("name"),
                        resultSet.getString("department"),
                        resultSet.getDouble("salary"));

                return foundEmployee;
            }
        } //Catch Block to handle Exceptions in Runtime
        catch (SQLException e) {
            System.out.println("An Error Occurred while Fetching Employee " +
                    "Detains into Database." + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("An Error Occurred while Closing the Connection" + e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    //This method is designed to generate all the Employees in the Database belongs to particular Department
    public void generateDepartmentReport(String departmentName) {

        //Got Connection
        Connection connection = dataBaseConnection.getDataBaseConnection();
        //Converting the received String to lower case
        String tempDepartmentName = departmentName.toLowerCase();
        tempDepartmentName = tempDepartmentName.strip();

        ResultSet resultSetForAverageSalary;
        ResultSet resultSetForEmployeeList;
        ResultSet resultSetForCount;
        try {
            //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
            PreparedStatement preparedStatementForCount = connection.prepareStatement(COUNT_OF_EMPLOYEES_BY_DEPT_NAME);
            preparedStatementForCount.setString(1, tempDepartmentName);
            //ResultSet for storing count of Employees Based on department
            resultSetForCount = preparedStatementForCount.executeQuery();
            int countOfEmployees = 0;
            if (resultSetForCount.next())
                // retrieved the count and stored in a variable
                countOfEmployees = resultSetForCount.getInt("count");

            //Checking weather the Employees are present in the Particular Department or not
            if (countOfEmployees != 0) {
                //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
                PreparedStatement preparedStatementForEmployeeList;
                preparedStatementForEmployeeList = connection.prepareStatement(FETCH_EMPLOYEES_BY_DEPT_NAME);
                preparedStatementForEmployeeList.setString(1, tempDepartmentName);
                //ResultSet for storing Employee Based on department
                resultSetForEmployeeList = preparedStatementForEmployeeList.executeQuery();
                //Checking weather the data is present in next row or not
                while (resultSetForEmployeeList.next()) {
                    //Printing the EmployeeDetails iin the console
                    System.out.println("Employee Id : " + resultSetForEmployeeList.getString("employee_id") +
                            " | Employee Name : " + resultSetForEmployeeList.getString("name") +
                            " | Salary : " + resultSetForEmployeeList.getDouble("salary"));
                }
                //Displaying the count of Employees
                System.out.println();
                System.out.println("Total number of Employees : " + countOfEmployees);
                System.out.println();

                //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
                PreparedStatement preparedStatementForAverageSalary;
                preparedStatementForAverageSalary = connection.prepareStatement(AVG_SALARY_OF_EMPLOYEES_BY_DEPT_NAME);
                preparedStatementForAverageSalary.setString(1, tempDepartmentName);
                //ResultSet for storing Employees Average Salary
                resultSetForAverageSalary = preparedStatementForAverageSalary.executeQuery();
                resultSetForAverageSalary.next();
                //Displaying the Average Salary of Employees in a Department
                System.out.println("Average Salary for the " + tempDepartmentName + " Department is : ₹" +
                        resultSetForAverageSalary.getDouble("average"));
                System.out.println();

            }//this will be executed when countOfEmployees is zero
            else {
                System.out.println("No Employees Found in the Specific Department");
            }
        } //Catch block to handle the Exceptions
        catch (SQLException e) {
            System.out.println("An Error Occurred while Preparing Department " +
                    "Report from Database." + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("An Error Occurred while Closing the Connection" + e.getMessage());
            }
        }
    }

    @Override
    //This method is designed to only Promote the Employees and not designed to for salary increment
    public void promoteEmployee(String employeeId, String employeeDepartment) {

        //Got Connection
        Connection connection = dataBaseConnection.getDataBaseConnection();
        //Removing the Spaces around the String by keeping spaces unchanged between two words
        String tempEmployeeId = employeeId.strip();
        tempEmployeeId = tempEmployeeId.toLowerCase();
        String tempEmployeeDepartment = employeeDepartment.strip();
        tempEmployeeDepartment = tempEmployeeDepartment.toLowerCase();

        if (!tempEmployeeId.isEmpty() && !tempEmployeeDepartment.isEmpty()) {

            Employee employee = findEmployeeById(tempEmployeeId);
            //Checking weather given EmployeeId is present or not
            if (employee != null) {
                boolean confirmDepartment = false;
                try {
                    //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
                    PreparedStatement pstmtForExistingDepartment;
                    pstmtForExistingDepartment = connection.prepareStatement(RETRIEVE_DEPARTMENT_BY_NAME_QUERY);
                    pstmtForExistingDepartment.setString(1, tempEmployeeDepartment);
                    //ResultSet for storing Department Based on Name
                    ResultSet resultSetForDepartment = pstmtForExistingDepartment.executeQuery();

                    //Storing a boolean value which refers to the presence of Data
                    confirmDepartment = resultSetForDepartment.next();

                } catch (SQLException e) {
                    System.out.println("An Error Occurred while Promoting the Employee" + e.getMessage());
                }
                //Checking weather given Department exist in department table or not
                if (confirmDepartment) {

                    try {
                        //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
                        PreparedStatement pstmtForUpdateDeptByEmployeeId;
                        pstmtForUpdateDeptByEmployeeId = connection.prepareStatement(UPDATE_DEPT_BY_EMPLOYEE_ID);
                        pstmtForUpdateDeptByEmployeeId.setString(1, tempEmployeeDepartment.toLowerCase());
                        pstmtForUpdateDeptByEmployeeId.setString(2, tempEmployeeId.toLowerCase());

                        //Storing the Number of Rows got Affected after executing the Query
                        int rowsAffectedForUpdatedEmployee = pstmtForUpdateDeptByEmployeeId.executeUpdate();

                        //Success block if Query gets executed without any errors or exceptions
                        if (rowsAffectedForUpdatedEmployee > 0) {
                            System.out.println();
                            System.out.println("Employee Promoted Successfully");
                            System.out.println();
                        }

                    } catch (SQLException e) {
                        System.out.println("An Error Occurred while Promoting the Employee" + e.getMessage());
                    }  finally {
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            System.out.println("An Error Occurred while Closing the Connection" + e.getMessage());
                        }
                    }
                } //Else block if provided Department is not Valid.
                else {
                    System.out.println();
                    System.out.println("Invalid Department, Please Try Again");
                    System.out.println();
                    //Closing the Connection
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        System.out.println("An Error Occurred while Closing the Connection" + e.getMessage());
                    }

                }// Else block if provided Employee id is not Valid.
            } else {
                System.out.println();
                System.out.println("Employee Not found with Id : " + tempEmployeeId + " please try again");
                System.out.println();
                //Closing the Connection
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
    }

    @Override
    //This method is designed to only Increment Salary of the Employees and not to promote the employees
    public void incrementEmployee(String employeeId, double salary) {

        //Got Connection
        Connection connection = dataBaseConnection.getDataBaseConnection();
        //Removing the Spaces around the String by keeping spaces unchanged between two words
        String tempEmployeeId = employeeId.strip();
        tempEmployeeId = tempEmployeeId.toLowerCase();

        if (!tempEmployeeId.isEmpty()) {

            Employee employee = findEmployeeById(tempEmployeeId);
            //Checking weather given EmployeeId is present or not
            if (employee != null) {

                //Checking weather provided salary is higher than current salary or not
                if (employee.getSalary() < salary) {

                    try {
                        //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
                        PreparedStatement pstmtForUpdateSalaryByEmployeeId;
                        pstmtForUpdateSalaryByEmployeeId = connection.prepareStatement(UPDATE_SALARY_BY_EMPLOYEE_ID);
                        pstmtForUpdateSalaryByEmployeeId.setString(2, tempEmployeeId.toLowerCase());
                        pstmtForUpdateSalaryByEmployeeId.setDouble(1, salary);

                        //Storing the boolean value which refers to the data in ResultSet
                        int rowsAffectedForUpdatedEmployee = pstmtForUpdateSalaryByEmployeeId.executeUpdate();

                        //Success Block if Query gets Executed
                        if (rowsAffectedForUpdatedEmployee > 0) {
                            System.out.println();
                            System.out.println("Employee Salary has been Incremented Successfully");
                            System.out.println();
                            connection.close();
                        }
                    } catch (SQLException e) {
                        System.out.println("An Error Occurred while Incrementing the Employee Salary" + e.getMessage());
                    }finally {
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            System.out.println("An Error Occurred while Closing the Connection" + e.getMessage());
                        }
                    }
                } //Else block if provided Salary is less than current one
                else {
                    System.out.println("Invalid Salary, Please provide a higher salary than the " +
                            "current salary (" + employee.getSalary() + ").");
                }
            } //Else block if provided Employee id is not Valid.
            else {
                System.out.println("Employee Not found with Id : " + tempEmployeeId + " please try again");
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
    }

    @Override
    //This method is designed to Promote and Increment the Salary of Employees
    public void promoteAndIncrementEmployee(Employee promotedEmployee) {

        //Got Connection
        Connection connection = dataBaseConnection.getDataBaseConnection();
        //Removing the Spaces around the String by keeping spaces unchanged between two words
        String tempEmployeeId = promotedEmployee.getEmployeeId().strip().toLowerCase();
        String tempEmployeeDepartment = promotedEmployee.getDepartment().strip().toLowerCase();
        double incrementedSalary = promotedEmployee.getSalary();

        if (!tempEmployeeId.isEmpty() && !tempEmployeeDepartment.isEmpty()) {

            Employee employee = findEmployeeById(tempEmployeeId);
            //Checking weather given EmployeeId is present or not
            if (employee != null) {

                boolean confirmDepartment = false;
                try {
                    //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
                    PreparedStatement pstmtForExistingDepartment;
                    pstmtForExistingDepartment = connection.prepareStatement(RETRIEVE_DEPARTMENT_BY_NAME_QUERY);
                    pstmtForExistingDepartment.setString(1, tempEmployeeDepartment.toLowerCase());
                    //ResultSet for storing Department Based on Name
                    ResultSet resultSetForDepartment = pstmtForExistingDepartment.executeQuery();

                    //Storing a boolean value which refers to the data in a ResultSet
                    confirmDepartment = resultSetForDepartment.next();
                    resultSetForDepartment.close();


                } catch (SQLException e) {
                    System.out.println("An Error Occurred while Promoting and" +
                            " Incrementing the Employee Salary" + e.getMessage());
                }
                //Checking weather given Department exist in department table or not
                if (confirmDepartment) {
                    //Checking weather provided salary is higher than current salary or not
                    if (employee.getSalary() < incrementedSalary) {

                        try {
                            //Prepared Statement using Connection and Setting the dynamic values which are
                            // received as arguments
                            PreparedStatement pstmtForUpdateDept;
                            pstmtForUpdateDept = connection.prepareStatement(UPDATE_DEPT_AND_SALARY_BY_EMPLOYEE_ID);
                            pstmtForUpdateDept.setString(1, tempEmployeeDepartment.toLowerCase());
                            pstmtForUpdateDept.setString(3, tempEmployeeId.toLowerCase());
                            pstmtForUpdateDept.setDouble(2, incrementedSalary);

                            //Storing number of rows affected when query is executed
                            int rowsAffectedForUpdatedEmployee = pstmtForUpdateDept.executeUpdate();

                            //Checking Weather the record is updated or not
                            if (rowsAffectedForUpdatedEmployee > 0) {
                                System.out.println();
                                System.out.println("Employee Promoted and Incremented Successfully");
                                System.out.println();
                                connection.close();
                            }
                        } catch (SQLException e) {
                            System.out.println("An Error Occurred while Promoting and Incrementing the Employee Salary" + e.getMessage());
                        }

                    }//Else block if provided Salary is less than current one
                    else {
                        System.out.println();
                        System.out.println("Invalid Salary, Please provide a higher salary than " +
                                "the current salary (" + employee.getSalary() + ").");
                        System.out.println();
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            System.out.println("An Error Occurred while Closing the Connection" + e.getMessage());
                        }
                    }
                }//  Else block if provided Department is not Valid.
                else {
                    System.out.println();
                    System.out.println("Invalid Department, Please Try Again");
                    System.out.println();
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        System.out.println("An Error Occurred while Closing the Connection" + e.getMessage());
                    }
                }
            } // Else block if provided Employee id is not Valid.
            else {
                System.out.println();
                System.out.println("Employee Not found with Id : " + tempEmployeeId + " please try again");
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

        //Got Connection
        Connection connection = dataBaseConnection.getDataBaseConnection();
        //Removing the Spaces around the String by keeping spaces unchanged between two words
        String tempEmployeeId = employeeId.strip();
        tempEmployeeId = tempEmployeeId.toLowerCase();

        //Checking whether the provided employeeId is empty or it contains any value
        if (!tempEmployeeId.isEmpty()) {

            Employee employee = findEmployeeById(tempEmployeeId);
            if (employee != null) {
                //initialising the PaySlipDaoImplementation to call methods inside it
                PaySlipDaoImpl paySlipDaoImpl = new PaySlipDaoImpl();

                Payslip payslip = paySlipDaoImpl.findPaySlip(tempEmployeeId);
                if (payslip != null) {

                    try {
                        recoveryPaySlipDaoImpl.movePaySlips(tempEmployeeId);

                        //Prepared Statement using Connection and Setting the dynamic values which are
                        // received as arguments
                        PreparedStatement pstmtForDeletingEmployee;
                        pstmtForDeletingEmployee = connection.prepareStatement(DELETE_EMPLOYEE_BY_ID_QUERY);
                        pstmtForDeletingEmployee.setString(1, tempEmployeeId);
                        int rowsDeleted = pstmtForDeletingEmployee.executeUpdate();

                        //Success Block if query gets Executed Properly
                        if (rowsDeleted > 0) {
                            System.out.println();
                            System.out.println("Employee Removed Successfully");
                            System.out.println();
                        }

                    } catch (SQLException e) {
                        System.out.println("An Error Occurred while Removing the Employee" + e.getMessage());
                    }  finally {
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            System.out.println("An Error Occurred while Closing the Connection" + e.getMessage());
                        }
                    }
                } //Else block to settle the PaySlips
                else {
                    System.out.println();
                    System.out.println("Cannot Remove Employee due to Pending Operations");
                    System.out.println();
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            System.out.println("An Error Occurred while Closing the Connection" + e.getMessage());
                        }
                }
            } // Else Block for Not found Employee
            else {
                System.out.println();
                System.out.println("Employee Not Found, Please try Again");
                System.out.println();
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
    }

    @Override
    //This method is designed to update the Existing Employee Details
    public boolean updateEmployeeById(Employee employee) {

        //Got Connection
        Connection connection = dataBaseConnection.getDataBaseConnection();

                boolean existingEmployee = isEmployeeExists(employee.getEmployeeId());
                boolean existingDepartment = isDepartmentExists(employee.getDepartment());

                //Checking weather the given employee id and department exists in the database or not
                if (existingEmployee && existingDepartment) {
                    try {
                        //Prepared Statement using Connection and Setting the dynamic values which are received as arguments
                        PreparedStatement pstmtForUpdatingEmployee = connection.prepareStatement(UPDATE_EMPLOYEE_QUERY);
                        pstmtForUpdatingEmployee.setString(4, employee.getEmployeeId());
                        pstmtForUpdatingEmployee.setString(1, employee.getName());
                        pstmtForUpdatingEmployee.setString(2, employee.getDepartment());
                        pstmtForUpdatingEmployee.setDouble(3, employee.getSalary());

                        //initialising the number of rows Affected after executing the Query
                        int rowsAffected = pstmtForUpdatingEmployee.executeUpdate();
                        //Checking weather the data is inserted or not
                        if (rowsAffected > 0) {
                            return true;
                        }
                    } catch (SQLException e) {
                        System.out.println("An Error Occurred while Updating the Employee Details" + e.getMessage());
                    }  finally {
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            System.out.println("An Error Occurred while Closing the Connection" + e.getMessage());
                        }
                    }
                }
            return false;
    }
}
