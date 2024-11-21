package Payslips;

public class PaySlipService {

    //A method designed to calculate the tax paid by an Employee
    double calculateTax(double salary) {
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

}
