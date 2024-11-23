package service;

import dao.EmployeeDaoImpl;
import model.Employee;

public class ValidationService {

    public Employee validateEmployeeData(Employee employee) {

        //Removing the Spaces around the String by keeping spaces unchanged between two words
        String employeeId = employee.getEmployeeId().strip().toLowerCase();
        String employeeName = employee.getName().strip().toLowerCase();
        String employeeDepartment = employee.getDepartment().strip().toLowerCase();
        double employeeSalary = employee.getSalary();

        //conditions to check or validate all the data is passed correctly or not
        if (!employeeId.isEmpty() && employeeName.length() > 1 &&
                !employeeDepartment.isEmpty() && employeeSalary > 0) {
            return new Employee(employeeId, employeeName, employeeDepartment, employeeSalary);
        } else {
            return null;
        }
    }

    public boolean insertEmployee(Employee employee) {

        EmployeeDaoImpl employeeDaoImpl = new EmployeeDaoImpl();

        Employee validatedEmployee = validateEmployeeData(employee);

        if (validatedEmployee != null) {
            boolean isEmployeeAdded = employeeDaoImpl.addEmployee(validatedEmployee);

            if (isEmployeeAdded) {
                System.out.println();
                System.out.println("Employee Added Successfully");
                System.out.println();
                return true;
            } else {
                System.out.println();
                System.out.println("Invalid Department or Duplicate EmployeeId Please try Again");
                System.out.println();
                return false;
            }
        } else {
            System.out.println();
            System.out.println("Invalid input or Duplicate EmployeeId Please try Again");
            System.out.println();
            return false;
        }
    }

    public boolean changeEmployeeDetails(Employee employee) {

        EmployeeDaoImpl employeeDaoImpl = new EmployeeDaoImpl();

        Employee validatedEmployee = validateEmployeeData(employee);

        if (validatedEmployee != null) {
            boolean isEmployeeUpdated = employeeDaoImpl.updateEmployeeById(validatedEmployee);

            if (isEmployeeUpdated) {
                System.out.println();
                System.out.println("Employee Updated Successfully");
                System.out.println();
                return true;
            } else {
                System.out.println();
                System.out.println("Invalid Department or Duplicate EmployeeId Please try Again");
                System.out.println();
                return false;
            }
        } else {
            System.out.println();
            System.out.println("Invalid input or Duplicate EmployeeId Please try Again");
            System.out.println();
            return false;
        }
    }

    public String validateEmployeeId(String employeeId) {
        String validatedEmployeeId = employeeId.strip().toLowerCase();

        if (!validatedEmployeeId.isEmpty()) {
            return validatedEmployeeId;
        }
        return null;
    }

    public boolean getEmployeeById(Employee employee) {

        String employeeId = validateEmployeeId(employee.getEmployeeId());



    }


}
