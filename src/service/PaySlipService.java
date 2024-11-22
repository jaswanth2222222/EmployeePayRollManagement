package service;

import model.Employee;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class PaySlipService {

    //A method designed to calculate the tax paid by an Employee
    public double calculateTax(double salary) {
        double tax;

        //Dividing the Salary into slabs and calculated the tax by assuming the tax percentage
        if (salary < 299999) {
            tax = 0 * salary;
        } else if (salary < 699999) {
            tax = 0.05 * salary;
        } else if (salary < 1199999) {
            tax = 0.12 * salary;
        } else if (salary < 1599999) {
            tax = 0.15 * salary;
        } else {
            tax = 0.20 * salary;
        }
        //returning tax
        return tax;

    }

    public void generatePaySlipFile(Employee employee, HashMap<String, Double> financeMap) {

        //Using System millis to keep the separate file for each one
        String filename = System.currentTimeMillis() + employee.getName() + ".word";
        double grossSalary = financeMap.get("grossSalary");
        double pfDeductions = financeMap.get("pfDeductions");
        double tax = financeMap.get("tax");
        double netSalary = financeMap.get("netSalary");

        try {
            //Initialising the BufferedWriter class and opened a Word file named PaySlips.word in append mode
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));
            writer.write("\n \n PaySlip for the Employee with Id :" + employee.getEmployeeId());
            writer.write("\n Employee Name : " + employee.getName());
            writer.write("\n Employee Department : " + employee.getDepartment());
            writer.write("\n Employee Gross Salary : " + grossSalary);
            writer.write("\n Employee Total Deduction towards Pf : " + pfDeductions);
            writer.write("\n Employee Total Deduction towards Tax : " + tax);
            writer.write("\n Employee Net or InHand Salary : " + netSalary);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
