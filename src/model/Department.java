package model;

public class Department {

    //Declaring the Column names and their DataTypes as private Variables

    private int departmentId;
    private String name;

    public Department() {
    }

    //Constructor with Arguments to initialise data and access the data if needed

    public Department(int departmentId, String name) {
        this.name = name;
        this.departmentId = departmentId;
    }

    //Getters and Setters for Retrieving and Modifying the data respectively in private variables
    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
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
                "departmentId=" + departmentId +
                ", name='" + name + '\'' +
                '}';
    }
}
