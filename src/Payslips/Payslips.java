package Payslips;

import java.time.LocalDate;

public class Payslips {

    //Declaring the Column names and their DataTypes ans private Variables

    private int payslip_id;
    private String employee_id;
    private double gross_salary;
    private double deductions;
    private double net_salary;
    private LocalDate date_generated;

    public Payslips() {
    }

    //Constructor with Arguments to initialise data and access the data if needed

    public Payslips(int payslip_id, String employee_id, double gross_salary, double deductions, double net_salary, LocalDate date_generated) {
        this.payslip_id = payslip_id;
        this.employee_id = employee_id;
        this.gross_salary = gross_salary;
        this.deductions = deductions;
        this.net_salary = net_salary;
        this.date_generated = date_generated;
    }

    //Getters and Setters for Retrieving and Modifying the data in private variables respectively

    public int getPayslip_id() {
        return payslip_id;
    }

    public void setPayslip_id(int payslip_id) {
        this.payslip_id = payslip_id;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public double getGross_salary() {
        return gross_salary;
    }

    public void setGross_salary(double gross_salary) {
        this.gross_salary = gross_salary;
    }

    public double getDeductions() {
        return deductions;
    }

    public void setDeductions(double deductions) {
        this.deductions = deductions;
    }

    public double getNet_salary() {
        return net_salary;
    }

    public void setNet_salary(double net_salary) {
        this.net_salary = net_salary;
    }

    public LocalDate getDate_generated() {
        return date_generated;
    }

    public void setDate_generated(LocalDate date_generated) {
        this.date_generated = date_generated;
    }

    //toString() to print the data in the Object in the form of Map

    @Override
    public String
    toString() {
        return "Payslips{" +
                "payslip_id='" + payslip_id + '\'' +
                ", employee_id='" + employee_id + '\'' +
                ", gross_salary=" + gross_salary +
                ", deductions=" + deductions +
                ", net_salary=" + net_salary +
                ", date_generated=" + date_generated +
                '}';
    }
}
