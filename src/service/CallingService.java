package service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.spi.DirStateFactory.Result;

import dao.EmployeeDaoImpl;
import dao.PaySlipDaoImpl;
import dao.RecoveryPaySlipDaoImpl;
import model.Employee;
import model.Payslip;

public class CallingService {

	ValidationService validationService = new ValidationService();

	public boolean insertEmployee(Employee employee) {

		EmployeeDaoImpl employeeDaoImpl = new EmployeeDaoImpl();

		Employee validatedEmployee = validationService.validateEmployeeData(employee);

		if (validatedEmployee != null) {

			boolean isDepartmentExists = employeeDaoImpl.isDepartmentExists(employee.getDepartment());
			boolean isEmployeeExists = employeeDaoImpl.isEmployeeExists(employee.getEmployeeId());

			if (isDepartmentExists && !isEmployeeExists) {
				boolean isEmployeeAdded = employeeDaoImpl.addEmployee(validatedEmployee);

				if (isEmployeeAdded) {
					System.out.println();
					System.out.println("Employee Added Successfully");
					System.out.println();
					return true;
				}
			} else {
				System.out.println();
				System.out.println("Invalid input or Duplicate EmployeeId Please try Again");
				System.out.println();
				return false;
			}
		} else {
			System.out.println();
			System.out.println("Invalid input or Duplicate EmployeeId Please try Again");
			System.out.println();
			return false;
		}
		return false;
	}

	public boolean changeEmployeeDetails(Employee employee) {

		EmployeeDaoImpl employeeDaoImpl = new EmployeeDaoImpl();

		Employee validatedEmployee = validationService.validateEmployeeData(employee);
		boolean existingEmployee = employeeDaoImpl.isEmployeeExists(employee.getEmployeeId());
		boolean existingDepartment = employeeDaoImpl.isDepartmentExists(employee.getDepartment());

		if (validatedEmployee != null) {

			if (existingEmployee && existingDepartment) {
				boolean isEmployeeUpdated = employeeDaoImpl.updateEmployeeById(validatedEmployee);

				if (isEmployeeUpdated) {
					System.out.println();
					System.out.println("Employee Updated Successfully");
					System.out.println();
					return true;
				}
			} else {
				System.out.println();
				System.out.println("Invalid input or Duplicate EmployeeId Please try Again");
				System.out.println();
				return false;
			}
		} else {
			System.out.println();
			System.out.println("Invalid input or Duplicate EmployeeId Please try Again");
			System.out.println();
			return false;
		}
		return false;
	}

	public boolean getEmployeeById(String employeeId) {

		String validatedEmployeeId = validationService.validateInput(employeeId);

		EmployeeDaoImpl employeeDaoImpl = new EmployeeDaoImpl();

		if (validatedEmployeeId != null) {
			Employee foundEmployee = employeeDaoImpl.findEmployeeById(validatedEmployeeId);

			System.out.println(foundEmployee.toString());
			return true;
		}

		return false;

	}

	public boolean removeEmployee(String employeeId) {

		// Removing the Spaces around the String by keeping spaces unchanged between two
		// words
		String validatedEmployeeId = employeeId.strip().toLowerCase();

		EmployeeDaoImpl employeeDaoImpl = new EmployeeDaoImpl();
		PaySlipDaoImpl paySlipDaoImpl = new PaySlipDaoImpl();

		if (validatedEmployeeId == null) {

			System.out.println();
			System.out.println("Employee Not Found, Please try Again");
			System.out.println();
			return false;

		}
		Employee employee = employeeDaoImpl.findEmployeeById(validatedEmployeeId);
		Payslip payslip = paySlipDaoImpl.findPaySlip(validatedEmployeeId);

		if (employee == null) {
			System.out.println();
			System.out.println("Employee not Found Please try Again.");
			System.out.println();
			return false;
		}

		if (payslip == null) {
			System.out.println();
			System.out.println("Employee not Removed due to Pending Operations");
			System.out.println();
			return false;
		}
		RecoveryPaySlipDaoImpl recoveryPaySlipDaoImpl = new RecoveryPaySlipDaoImpl();

		boolean isPayslipsMoved = recoveryPaySlipDaoImpl.movePaySlips(validatedEmployeeId);
		boolean isPayslipsDeleted = false;
		if (isPayslipsMoved) {
			isPayslipsDeleted = paySlipDaoImpl.deletePaySlips(validatedEmployeeId);
		}
		boolean isEmployeeDeleted = false;
		if (isPayslipsDeleted) {

			isEmployeeDeleted = employeeDaoImpl.deleteEmployee(validatedEmployeeId);

			if (isEmployeeDeleted) {
				System.out.println();
				System.out.println("Employee Removed Successfully");
				System.out.println();
				return true;
			}
		}
		return isEmployeeDeleted;
	}

	public boolean generateDepartmentReport(String departmentName) {

		EmployeeDaoImpl employeeDaoImpl = new EmployeeDaoImpl();
		String validatedDept = validationService.validateInput(departmentName);
		int countOfEmployees;
		double averageSalary;

		if (validatedDept != null) {
			countOfEmployees = employeeDaoImpl.getCountOfEmployeesInDept(validatedDept);

			if (countOfEmployees > 0) {
				averageSalary = employeeDaoImpl.getAverageSalaryOfDepartment(validatedDept);

				ArrayList<Employee> listOfEmployees = employeeDaoImpl.getAllEmployeesByDeptName(validatedDept);

				listOfEmployees.stream().forEach(item -> System.out.println(item));
				
				System.out.println("Total Number of Employees in " + departmentName + " is : " + countOfEmployees);
				System.out.println();
				System.out.println("Average Salary Provided by the Department is : " + averageSalary);
				System.out.println();
			}

		} else {
			System.out.println();
			System.out.println("Entered Department Not found try again");
			System.out.println();
		}

		return false;
	}

	public boolean promoteEmployee(String employeeId, String departmentName) {

		String validatedEmployeeId = validationService.validateInput(employeeId);
		String validatedDeptName = validationService.validateInput(departmentName);

		EmployeeDaoImpl employeeDaoImpl = new EmployeeDaoImpl();

		if (validatedDeptName != null && validatedEmployeeId != null) {
			boolean isEmployeeExists = employeeDaoImpl.isEmployeeExists(validatedEmployeeId);
			boolean isDepartmentExists = employeeDaoImpl.isDepartmentExists(validatedDeptName);

			if (isDepartmentExists && isEmployeeExists) {
				boolean isEmployeePromoted = employeeDaoImpl.promoteEmployee(validatedEmployeeId, validatedDeptName);

				if (isEmployeePromoted) {
					System.out.println();
					System.out.println("Employee Promoted Successfully");
					System.out.println();
					return true;
				}
			} else {
				System.out.println();
				System.out.println("Please Provide valid Department or Employee Id");
				System.out.println();
				return false;
			}
		} else {
			System.out.println();
			System.out.println("Please Provide Valid Input");
			System.out.println();
			return false;
		}
		return false;
	}

	public boolean incrementEmployee(String employeeId, double salary) {

		String validatedEmployeeId = validationService.validateInput(employeeId);

		EmployeeDaoImpl employeeDaoImpl = new EmployeeDaoImpl();

		if (validatedEmployeeId == null || salary < 0) {
			System.out.println();
			System.out.println("Please Enter Valid Employee Id Or Salary.");
			System.out.println();
			return false;
		}

		Employee employee = employeeDaoImpl.findEmployeeById(validatedEmployeeId);
		if (employee == null) {
			System.out.println();
			System.out.println("Employee not found with Id : " + employeeId + ", Please try Another.");
			System.out.println();
			return false;
		}

		if (employee.getSalary() > salary) {
			System.out.println();
			System.out.println("Please Enter Salary Greater than Current one.");
			System.out.println();
			return false;
		}

		boolean isEmployeeIncremented = employeeDaoImpl.incrementEmployee(validatedEmployeeId, salary);
		if (isEmployeeIncremented) {
			System.out.println();
			System.out.println("Employee Salary Incremented Successfully.");
			System.out.println();
			return true;
		}
		return false;
	}

	public boolean promoteAndIncrementEmployee(String employeeId, String departmentName, double salary) {

		String validatedEmployeeId = validationService.validateInput(employeeId);
		String validatedDeptName = validationService.validateInput(departmentName);

		EmployeeDaoImpl employeeDaoImpl = new EmployeeDaoImpl();

		if (validatedDeptName == null || validatedEmployeeId == null || salary < 0) {
			System.out.println();
			System.out.println("Please Enter Valid data.");
			System.out.println();
			return false;
		}

		Employee employee = employeeDaoImpl.findEmployeeById(validatedEmployeeId);
		if (employee.getSalary() > salary) {
			System.out.println();
			System.out.println("Please Enter Salary Greater than Current one.");
			System.out.println();
			return false;
		}
		boolean isDepartmentExists = employeeDaoImpl.isDepartmentExists(validatedDeptName);
		if (!isDepartmentExists) {
			System.out.println();
			System.out.println("Please Enter Valid Department");
			System.out.println();
			return false;
		}

		boolean isEmployeePromotedAndIncrement = employeeDaoImpl.promoteAndIncrementEmployee(validatedEmployeeId,
				validatedDeptName, salary);

		if (isEmployeePromotedAndIncrement) {
			System.out.println();
			System.out.println("Employee Promoted and Incremented Successfully.");
			System.out.println();
			return true;
		}

		return false;
	}

	public boolean generatePaySlip(String employeeId) {

		String validatedEmployeeID = validationService.validateInput(employeeId);

		if (validatedEmployeeID == null) {
			System.out.println();
			System.out.println("Please Enter Valid Employee Id");
			System.out.println();
			return false;
		}
		EmployeeDaoImpl employeeDaoImpl = new EmployeeDaoImpl();

		Employee employee = employeeDaoImpl.findEmployeeById(validatedEmployeeID);

		if (employee == null) {
			System.out.println();
			System.out.println("Employee not Found, Please try Again.");
			System.out.println();
		}
		PaySlipService paySlipService = new PaySlipService();
		PaySlipDaoImpl paySlipDaoImpl = new PaySlipDaoImpl();
		Payslip payslip = new Payslip();
		double grossSalary = employee.getSalary();
		payslip.setGrossSalary(grossSalary);
		double tax = paySlipService.calculateTax(grossSalary);
		double pfDeductions = 0.12 * grossSalary;
		double totalDeductions = tax + pfDeductions;
		payslip.setDeductions(totalDeductions);
		// calculating net salary
		double netSalary = grossSalary - totalDeductions;
		payslip.setNetSalary(netSalary);
		payslip.setDateGenerated(LocalDate.now());
		payslip.setEmployeeId(validatedEmployeeID);

		boolean isPayslipGenerated = paySlipDaoImpl.generatePaySlip(payslip);
		boolean isFileGenerated = false;

		if (isPayslipGenerated) {
			HashMap<String, Double> financeMap = new HashMap<String, Double>();
			financeMap.put("grossSalary", grossSalary);
			financeMap.put("pfDeductions", pfDeductions);
			financeMap.put("tax", tax);
			financeMap.put("netSalary", netSalary);
			isFileGenerated = paySlipService.generatePaySlipFile(employee, financeMap);
		}
		if (isFileGenerated) {
			System.out.println();
			System.out.println("PaySlip Generated for the Employee with Id : " + employeeId);
			System.out.println();
			return true;
		}

		return false;
	}
}
