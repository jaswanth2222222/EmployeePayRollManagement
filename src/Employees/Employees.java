package Employees;

public class Employees {

    //Declaring the Column names and their DataTypes as private Variables and named exactly as column names

    private String employee_id;
    private String name;
    private String department;
    private double salary;

    //Constructor with Arguments to initialise data and access the data if needed

    public Employees(String employee_id, String name, String department, double salary) {
        this.employee_id = employee_id;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    //Getters and Setters for Retrieving and Modifying the data in private variables respectively

    public Employees() {
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
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
                "employee_id='" + employee_id + '\'' +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", salary=" + salary +
                '}';
    }
}
