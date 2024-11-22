package model;

import java.time.LocalDate;

public class Payslip {

    //Declaring the Column names and their DataTypes ans private Variables

    private int payslipId;
    private String employeeId;
    private double grossSalary;
    private double deductions;
    private double netSalary;
    private LocalDate dateGenerated;

    public Payslip() {
    }

    //Constructor with Arguments to initialise data and access the data if needed

    public Payslip(int payslipId, String employeeId, double grossSalary, double deductions, double netSalary, LocalDate dateGenerated) {
        this.payslipId = payslipId;
        this.employeeId = employeeId;
        this.grossSalary = grossSalary;
        this.deductions = deductions;
        this.netSalary = netSalary;
        this.dateGenerated = dateGenerated;
    }

    //Getters and Setters for Retrieving and Modifying the data in private variables respectively

    public int getPayslipId() {
        return payslipId;
    }

    public void setPayslipId(int payslipId) {
        this.payslipId = payslipId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public double getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(double grossSalary) {
        this.grossSalary = grossSalary;
    }

    public double getDeductions() {
        return deductions;
    }

    public void setDeductions(double deductions) {
        this.deductions = deductions;
    }

    public double getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(double netSalary) {
        this.netSalary = netSalary;
    }

    public LocalDate getDateGenerated() {
        return dateGenerated;
    }

    public void setDateGenerated(LocalDate dateGenerated) {
        this.dateGenerated = dateGenerated;
    }

    //toString() to print the data in the Object in the form of Map

    @Override
    public String
    toString() {
        return "Payslips{" +
                "payslip_id='" + payslipId + '\'' +
                ", employee_id='" + employeeId + '\'' +
                ", gross_salary=" + grossSalary +
                ", deductions=" + deductions +
                ", net_salary=" + netSalary +
                ", date_generated=" + dateGenerated +
                '}';
    }
}
