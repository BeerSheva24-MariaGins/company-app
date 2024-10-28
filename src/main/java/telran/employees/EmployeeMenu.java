package telran.employees;

import org.json.JSONObject;
import telran.view.*;
import java.util.Arrays;
import java.util.HashSet;

public class EmployeeMenu {
    private static Company company;
    final static long MIN_ID = 10;
    final static long MAX_ID = 999;
    final static int MIN_SALARY = 10000;
    final static int MAX_SALARY = 100000;
    final static String[] DEPARTMENTS = { "QA", "Development", "Audit", "HR" };
    final static HashSet<String> departmentsSet = new HashSet<>(Arrays.asList(DEPARTMENTS));

    final static int MIN_WAGE = 10;
    final static int MAX_WAGE = 100;
    final static int MIN_HOURS = 0;
    final static int MAX_HOURS = 200;

    final static float MIN_PERCENT = 0.01f;
    final static float MAX_PERCENT = 0.5f;
    final static long MIN_SALES = 0;
    final static long MAX_SALES = 999;

    final static float MIN_FACTOR = 1;
    final static float MAX_FACTOR = 9;

    public static void performEmployeeMenu(InputOutput io, Company company) {
        EmployeeMenu.company = company;
        Item[] employeeType = {
                Item.of("Employee", EmployeeMenu::addEmployeeLine),
                Item.of("Wage employee", EmployeeMenu::addEmployeeWage),
                Item.of("Manager", EmployeeMenu::addEmployeeManager),
                Item.of("Sales person", EmployeeMenu::addEmployeeSales),
                Item.ofGoBack()
        };
        Menu subMenu = new Menu("Choose type of employee", employeeType);
        subMenu.perform(io);
    }

    static JSONObject addEmployeeBase(InputOutput io) {
        long id = io.readNumberRange("Enter employee ID in range between " + MIN_ID + " and " + MAX_ID + ":",
                "Invalid ID, must be a number between " + MIN_ID + " and " + MAX_ID, MIN_ID, MAX_ID).longValue();
        String department = io.readStringOptions("Enter company department: \"QA\", \"Development\", \"Audit\", \"HR\"",
                "Department doesn't exist in company", new HashSet<>(Arrays.asList(DEPARTMENTS)));
        int basicSalary = io.readNumberRange(
                "Enter the employee salary in range between " + MIN_SALARY + " and " + MAX_SALARY + ":",
                "The salary is out of range of company salary limit", MIN_SALARY, MAX_SALARY).intValue();
        JSONObject jsonBase = new JSONObject(
                String.format("{\"id\":%d, \"basicSalary\":%d, \"department\":%s}", id, basicSalary, department));
        return jsonBase;
    }

    static JSONObject makeWageEmployee(InputOutput io) {
        JSONObject jsonWage = addEmployeeBase(io);
        int hours = io.readInt(
                "Enter the extra hours of employee in range between " + MIN_HOURS + " and " + MAX_HOURS + ":", "");
        int wage = io.readInt("Enter the employee wage in range between " + MIN_WAGE + " and " + MAX_WAGE + ":", "");
        jsonWage.put("hours", hours);
        jsonWage.put("wage", wage);
        return jsonWage;
    }

    static void addEmployeeLine(InputOutput io) {
        JSONObject jsonEmpl = addEmployeeBase(io).put("className", "telran.employees.Employee");
        company.addEmployee(Employee.getEmployeeFromJSON(jsonEmpl.toString()));
        io.writeLine("Employee was created successfully!");
    }

    static void addEmployeeWage(InputOutput io) {
        JSONObject jsonWage = makeWageEmployee(io).put("className", "telran.employees.WageEmployee");
        company.addEmployee(WageEmployee.getEmployeeFromJSON(jsonWage.toString()));
        io.writeLine("Wage employee was created successfully!");
    }

    static void addEmployeeManager(InputOutput io) {
        JSONObject jsonEmpl = addEmployeeBase(io).put("className", "telran.employees.Manager");
        float factor = io
                .readInt("Enter the factor of the manager in range between " + MIN_FACTOR + " and " + MAX_FACTOR + ":",
                        "")
                .floatValue();
        jsonEmpl.put("factor", factor);
        company.addEmployee(Manager.getEmployeeFromJSON(jsonEmpl.toString()));
        io.writeLine("Manager was created successfully!");
    }

    static void addEmployeeSales(InputOutput io) {
        JSONObject jsonSales = makeWageEmployee(io).put("className", "telran.employees.SalesPerson");
        float percent = io
                .readInt("Enter percent of sales person in range between " + MIN_PERCENT + " and " + MAX_PERCENT + ":",
                        "")
                .floatValue();
        long sales = io.readInt("Enter the sales in range between " + MIN_SALES + " and " + MAX_SALES + ":", "")
                .longValue();
        jsonSales.put("percent", percent);
        jsonSales.put("sales", sales);
        company.addEmployee(SalesPerson.getEmployeeFromJSON(jsonSales.toString()));
        io.writeLine("Sales person was created successfully!");
    }
}
