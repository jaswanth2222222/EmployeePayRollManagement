package service;

import java.util.Scanner;

import dao.EmployeeDaoImpl;
import dao.PaySlipDaoImpl;
import model.Employee;

public class UIService {

	public void performOperation() {

		// Initialising the EmployeeDaoImplementation class to call the methods inside
		// it
//		EmployeeDaoImpl employeeDaoImpl = new EmployeeDaoImpl();
		// Initialising the PayslipService class to call the methods inside it
//		PaySlipDaoImpl paySlipDaoImpl = new PaySlipDaoImpl();
		// Initialising the Employee class to call the methods inside it
		Employee employee = new Employee();
		
		CallingService callingService = new CallingService();

		// Initialising the Scanner class to take dynamic inputs
		Scanner scanner = new Scanner(System.in);

		while (true) {

			// println() methods to make the user understand better
			System.out.println("Press 1 for Adding Employee");
			System.out.println("Press 2 to View Employee");
			System.out.println("Press 3 to Generate PaySlip for an Employee");
			System.out.println("Press 4 to Update Employee");
			System.out.println("Press 5 to Generate Department Report");
			System.out.println("Press 6 to Promote the Employee");
			System.out.println("Press 7 to Remove the Employee");
			System.out.println("Press 8 to Exit");
			System.out.println();

			// Asking User to Pick a Number
			System.out.print("Please pick a number : ");
			String strOpinion = scanner.next();

			int opinion;
			try {
				opinion = Integer.parseInt(strOpinion);
				if (opinion < 1 && opinion > 8) {
					System.out.println("Please Enter Number in given range.");
				}
			} catch (NumberFormatException e) {
				System.out.println();
				System.out.println("Please Pick an integer value.");
				System.out.println();
				continue;
			}
			// Checking weather User is interested to exit or not
			if (opinion == 8)
				// Breaking the loop if User entered a value to exit
				break;

			// Using Switch Case to check the User Opinion depending on the users opinion
			// the operation will be performed

			switch (opinion) {
			// Checking the User need using Switch case statements
			case 1:

				// println() methods for User Experience
				System.out.println();
				System.out.print("Enter Employee Id : ");
				scanner.nextLine();
				final String employeeId = scanner.nextLine(); // ""
				employee.setEmployeeId(employeeId);
				System.out.print("Enter Employee Name : ");
				final String employeeName = scanner.nextLine();
				employee.setName(employeeName);
				System.out.print("Enter Employee Department : ");
				final String employeeDepartment = scanner.nextLine();
				employee.setDepartment(employeeDepartment);
				System.out.print("Enter Employee Salary : ");
				final double employeeSalary = scanner.nextDouble();
				employee.setSalary(employeeSalary);

				// Calling addEmployee() by passing arguments
				callingService.insertEmployee(employee);

				break;
			case 2:

				// println() methods for User Experience
				System.out.println();
				System.out.print("Enter Employee Id : ");
				scanner.nextLine();
				final String employeeId1 = scanner.nextLine();

				// Calling getEmployeeByIId() by passing arguments
				callingService.getEmployeeById(employeeId1);

				break;
			case 3:

				// println() methods for User Experience
				System.out.println();
				System.out.print("Enter Employee Id to Generate PaySlip : ");
				scanner.nextLine();
				final String employeeId3 = scanner.nextLine();

				// Calling generatePaySlip() by passing arguments
				callingService.generatePaySlip(employeeId3);

				break;
			case 4:

				// println() methods for User Experience
				System.out.println();
				System.out.print("Enter Employee Id Need to Update: ");
				scanner.nextLine();
				final String employeeId4 = scanner.nextLine();
				employee.setEmployeeId(employeeId4);
				System.out.print("Enter Updated Employee Name : ");
				final String employeeName4 = scanner.nextLine();
				employee.setName(employeeName4);
				System.out.print("Enter Updated Employee Department : ");
				final String employeeDepartment4 = scanner.nextLine();
				employee.setDepartment(employeeDepartment4);
				System.out.print("Enter Updated Employee Salary : ");
				final double employeeSalary4 = scanner.nextDouble();
				employee.setSalary(employeeSalary4);

				// Calling updateEmployee() by passing arguments
				callingService.changeEmployeeDetails(employee);

				break;
			case 5:

				// println() methods for User Experience
				System.out.println();
				System.out.print("Enter Department Name to Generate Report: ");
				scanner.nextLine();
				final String departmentName5 = scanner.nextLine();

				// Calling generateDepartmentReport() by passing arguments
				callingService.generateDepartmentReport(departmentName5);

				break;

			case 6:

				// println() methods for User Experience
				System.out.println();
				System.out.print("Enter Employee Id to Promote or Increment of Salary : ");
				scanner.nextLine();
				final String employeeId6 = scanner.nextLine();

				// Variable declaration to store user opinion regarding the promotion of
				// Employee
				System.out.println(
						"    Press --p to promote, Press --i to increase salary or press --b to promote and increment");
				System.out.print("    Choose your option : ");
				String promoteOpinion = scanner.nextLine();
				promoteOpinion = promoteOpinion.toLowerCase();
				switch (promoteOpinion) {
				// Case for Promoting the Employee
				case "p":

					// println() methods for User Experience
					System.out.println();
					System.out.print("Enter Department name to Promote : ");
					final String departmentName6p = scanner.nextLine();

					callingService.promoteEmployee(employeeId6, departmentName6p);

					break;
				case "i":

					// println() methods for User Experience
					System.out.println();
					System.out.print("Enter Salary After increment : ");
					final double employeeSalary6i = scanner.nextDouble();

					callingService.incrementEmployee(employeeId6, employeeSalary6i);

					break;
				case "b":

					// println() methods for User Experience
					System.out.println();
					System.out.print("Enter Department name to Promote : ");
					final String departmentName6b = scanner.nextLine();
					System.out.println();
					System.out.print("Enter Salary After increment : ");
					final double employeeSalary6b = scanner.nextDouble();

					callingService.promoteAndIncrementEmployee(employeeId6, departmentName6b, employeeSalary6b);

					break;
				default:
					// println() methods for User Experience
					System.out.println();
					System.out.println("Please Choose among the given Options");
					System.out.println();
				}

				break;

			case 7:

				// println() methods for User Experience
				System.out.println();
				System.out.print("Enter Employee Id to Remove : ");
				scanner.nextLine();
				final String employeeId7 = scanner.nextLine();

				// Calling deleteEmployee method to remove the employee from database
				callingService.removeEmployee(employeeId7);

				break;

			// Default case if user enter other than provided numbers
			default:
				// println() methods for User Experience
				System.out.println();
				System.out.println("Please Enter a valid number..");
			}

		}
	}
	
	public void presentThankful() {
		// Presenting Thank you to the User
		System.out.println();
		System.out.println("Thank you for Using the Payroll Management System");
		System.out.println("Happy to Serve You...");
	}

}
