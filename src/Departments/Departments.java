package Departments;

public class Departments {

    //Declaring the Column names and their DataTypes as private Variables

    private int department_id;
    private String name;

    public Departments() {
    }

    //Constructor with Arguments to initialise data and access the data if needed

    public Departments(int department_id, String name) {
        this.name = name;
        this.department_id = department_id;
    }

    //Getters and Setters for Retrieving and Modifying the data respectively in private variables

    public int getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //toString() to print the data in the Object in the form of Map

    @Override
    public String toString() {
        return "Departments{" +
                "department_id=" + department_id +
                ", name='" + name + '\'' +
                '}';
    }
}
