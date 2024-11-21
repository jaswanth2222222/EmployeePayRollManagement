package Payslips;

public interface PaySlipDao {

    //Abstract method for Generating payslip to the Employee
    void generatePaySlip(String employeeId);

    //Abstract method for returning payslip of an Employee
    Payslips returnPaySlip(String employeeId);

    //Abstract method for deleting payslips of an employee
    void deletePaySlips(String employeeId);
}
