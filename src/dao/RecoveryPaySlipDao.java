package dao;

public interface RecoveryPaySlipDao {
    //Abstract method for Adding the PaySlip Details from payslips table to RecoveryPaySlips Table
    void movePaySlips(String employeeId);
}
