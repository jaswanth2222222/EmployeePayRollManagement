package dao;

import model.Payslip;

public interface PaySlipDao {

    //Abstract method for Generating payslip to the Employee
    void generatePaySlip(String employeeId);

    //Abstract method for returning payslip of an Employee
    Payslip findPaySlip(String employeeId);

    //Abstract method for deleting payslips of an employee
    void deletePaySlips(String employeeId);
}
