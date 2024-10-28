package telran.employees;

import telran.view.*;
import java.util.Arrays;
import java.util.HashSet;

public class CompanyMenu {
    private static Company company;
    final static long MIN_ID = 10;
    final static long MAX_ID = 999;
    final static int MIN_SALARY = 10000;
    final static int MAX_SALARY = 100000;
    final static String[] DEPARTMENTS = { "QA", "Development", "Audit", "HR" };
    final static HashSet<String> departmentsSet = new HashSet<>(Arrays.asList(DEPARTMENTS));

    public static Item[] getItems(Company company) {
        CompanyMenu.company = company;
        Item[] res = {
                Item.of("Add Employee", CompanyMenu::addEmployee),
                Item.of("Remove Employee", CompanyMenu::removeEmployee),
                Item.of("Get Employee by ID", CompanyMenu::getEmployeeById),
                Item.of("Get Department Budget", CompanyMenu::getDepartmentBudget),
                Item.of("Get Departments", CompanyMenu::getDepartments),
                Item.of("Get Managers with the Most Factor", CompanyMenu::getManagersWithMostFactor),
                Item.of("Save", CompanyMenu::saveToFile),
                Item.of("Restore", CompanyMenu::restoreFromFile),
                Item.ofExit()
        };
        return res;
    }

    static void addEmployee(InputOutput io) {
        EmployeeMenu.performEmployeeMenu(io, company);
    }

    static void removeEmployee(InputOutput io) {
        Long id = io.readLong("Enter employee ID in range between " + MIN_ID + " and " + MAX_ID + ": ",
                "Invalid ID, please repeat: ");
        Employee removedEmployee = company.removeEmployee(id);
        io.writeLine(String.format("Employee %s removed", removedEmployee));
    }

    static void getEmployeeById(InputOutput io) {
        Long id = io.readLong("Enter employee ID in range between " + MIN_ID + " and " + MAX_ID + ": ",
                "Invalid ID, please repeat: ");
        Employee employee = company.getEmployee(id);
        io.writeLine(employee == null ? "No employee found" : employee);
    }

    static void getDepartmentBudget(InputOutput io) {
        String department = io.readString("Enter department: \"QA\", \"Development\", \"Audit\", \"HR\"");
        try {
            int budget = company.getDepartmentBudget(department);
            io.writeLine("The budget is " + budget);
        } catch (Exception e) {
            io.writeLine("No such department in company");
        }
    }

    static void getDepartments(InputOutput io) {
        String[] departments = company.getDepartments();
        io.writeLine("The departments of company are: " + String.join(", ", departments));
    }

    static void getManagersWithMostFactor(InputOutput io) {
        Manager[] managers = company.getManagersWithMostFactor();
        io.writeLine("The managers with the most factor: \n");
        for (Manager manager : managers)
            io.writeLine(manager.toString());
        if (managers.length == 0)
            io.writeLine("No managers found");
    }

    static void saveToFile(InputOutput io) {
        ((CompanyImpl) company).saveToFile("employees.data");
        io.writeLine("Company has been saved to file");
    }

    static void restoreFromFile(InputOutput io) {
        String fileName = io.readString("Enter file to read from: ");
        CompanyImpl companyImpl = (CompanyImpl) company;
        companyImpl.restoreFromFile(fileName);
        companyImpl.saveToFile("employees.data");
        io.writeLine("Company data restored from " + fileName);
    }
}
