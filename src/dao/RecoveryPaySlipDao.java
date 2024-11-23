package dao;

public interface RecoveryPaySlipDao {
    //Abstract method for Adding the PaySlip Details from payslips table to RecoveryPaySlips Table
    @SuppressWarnings("unused")
    void movePaySlips(String employeeId);
}
