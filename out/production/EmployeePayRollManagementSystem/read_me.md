Language: Java
Database: MySql
Resources: mysql-connector-j-9.1.0.jar (This resource has been added in the ProjectStructure -> Modules -> Dependencies ->
                                        + -> add Jar file or dependency -> select file and apply)

DataBaseConnection.java :
        In this class getConnection() method will return a connection Object which is used to connect with database
        all the credentials in this class are private so there will be no access to another database from outside class.
        (To secure the username, password and url made a separate class)

departments.java, employees.java and PaySlips.java:
        These are the POJO classes with private Variables and getter setter methods to access the data indirectly

DepartmentsDao.java, PaySlipsDao.java and EmployeeDao.java :
        These are Interfaces that contains the abstract methods which are needed to be implemented to perform the given tasks
        or Requirements these are implemented by a class in the same package

DepartmentDaoImplementation.java, PaySlipDaoImplementation.java and EmployeeDaoImplementation.java :
        These classes will implement the Dao interfaces each method have its own significance and operations

PaySlipService.java
        This will act like a Service layer as it calculates the deductions of an employee salary which is business logic.
        Also used BufferedWriter to write the payslip details in the file.

MySql Commands :
    Employee table :
        create table employees (
        employee_id varchar(25) primary key,
        name varchar(255) not null,
        department varchar(255),
        salary decimal(10, 2) not null);

    departments table:
        create table departments (
        department_id int not null auto_increment primary key,
        name varchar(255) unique);

    payslips table:
        create table payslips(
        payslip_id int not null primary key auto_increment,
        employee_id varchar(25),
        gross_salary decimal(10, 2) not null,
        deductions decimal(10, 2) not null,
        net_salary decimal(10, 2) not null,
        date_generated date not null,
        Foreign key (employee_id) references employees(employee_id));

    RecoveryPaySlipsTable:
        create table payslips(
                payslip_id int not null,
                employee_id varchar(25),
                gross_salary decimal(10, 2) not null,
                deductions decimal(10, 2) not null,
                net_salary decimal(10, 2) not null,
                date_generated date not null);

                This Recovery table is used to store the payslips of the Deleted employees for any future Use and Structure of
                table is same as payslips but here there are no keys or constraints present in it


USAGE INSTRUCTIONS:
    1.Please follow the guidelines and pick a valid number in given range
    2.Please Provide Valid input for adding and updating the Employee
        2.1.Avoid spaces before and after names (you can keep in between)
        2.2.Avoid providing only spaces
        2.3.Ensure that name must be the minimum length of 2 while adding and updating the Employee
    3.Avoid using spaces in Department names
        3.1.There will be no spaces in department names
    4.Ensure that you are not passing empty data to any field
    5.Make Sure that before deleting employee paySlip is Generated
