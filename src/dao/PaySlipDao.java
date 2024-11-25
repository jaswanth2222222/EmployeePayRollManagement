package dao;

import model.Payslip;

public interface PaySlipDao {

    //Abstract method for Generating payslip to the Employee
    boolean generatePaySlip(Payslip payslip);

    //Abstract method for returning payslip of an Employee
    Payslip findPaySlip(String employeeId);

    //Abstract method for deleting payslips of an employee
    boolean deletePaySlips(String employeeId);
}
