package model;

public class Employee {

    //Declaring the Column names and their DataTypes as private Variables and named exactly as column names

    private String employeeId;
    private String name;
    private String department;
    private double salary;

    //Constructor with Arguments to initialise data and access the data if needed

    public Employee(String employeeId, String name, String department, double salary) {
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    //Getters and Setters for Retrieving and Modifying the data in private variables respectively

    public Employee() {
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    //toString() to print the data in the Object in the form of Map

    @Override
    public String toString() {
        return "Employees.Employees{" +
                "employee_id='" + employeeId + '\'' +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", salary=" + salary +
                '}';
    }
}
