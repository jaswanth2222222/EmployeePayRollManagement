package dao;

//this interface is implemented by the EmployeeDaoImpl class to make the application easy all the methods of
// Operation should be kept in Interfaces and later these will be implemented

import model.Employee;

public interface EmployeeDao {

    //Abstract method for Adding Employee to the Database
    void addEmployee(Employee employee);

    //Abstract method for Retrieving the Employee Details based on their Unique Employee id
    void getEmployeeById(String employeeId);

    //Abstract method for Updating existing Employee in the Database with their respective Employee id
    void updateEmployeeById(Employee employee);

    //Abstract method for Retrieving Employees with respect to their working Departments
    void generateDepartmentReport(String departmentName);

    //Abstract method for Promoting The Employee
    void promoteEmployee(String employeeId, String departmentName);

    //Abstract method for Incrementing the Employee Salary
    void incrementEmployee(String employeeId, double Salary);

    //Abstract method for Promoting and Incrementing the Employee
    void promoteAndIncrementEmployee(Employee employee);

    //Abstract method for Deleting the Employee from DataBase
    void deleteEmployee(String employeeId);

}
