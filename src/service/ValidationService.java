package service;

import model.Employee;

public class ValidationService {
	
	public Employee validateEmployeeData(Employee employee) {

		// Removing the Spaces around the String by keeping spaces unchanged between two
		// words
		String employeeId = employee.getEmployeeId().strip().toLowerCase();
		String employeeName = employee.getName().strip().toLowerCase();
		String employeeDepartment = employee.getDepartment().strip().toLowerCase();
		double employeeSalary = employee.getSalary();

		// conditions to check or validate all the data is passed correctly or not
		if (!employeeId.isEmpty() && employeeName.length() > 1 && !employeeDepartment.isEmpty() && employeeSalary > 0) {
			return new Employee(employeeId, employeeName, employeeDepartment, employeeSalary);
		} else {
			return null;
		}
	}
	
	public String validateInput(String strInput) {
		String validatedInput = strInput.strip().toLowerCase();

		if (!validatedInput.isEmpty()) {
			return validatedInput;
		}
		return null;
	}

}
