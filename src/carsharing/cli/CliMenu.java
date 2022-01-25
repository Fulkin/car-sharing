package carsharing.cli;

import carsharing.dao.database.UtilsDB;
import carsharing.dao.model.Car;
import carsharing.dao.model.Company;
import carsharing.dao.model.Customer;

import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

/**
 * @author Fulkin
 * Created on 21.01.2022
 */

public class CliMenu {

    private static CliMenu cliMenu = null;

    UtilsDB utilsDB;
    Scanner sc;

    private CliMenu(String dbFile) {
        sc = new Scanner(System.in);
        utilsDB = UtilsDB.getInstance();
        utilsDB.connectToDB(dbFile);
    }

    public static CliMenu getInstance(String dbFile) {
        if (cliMenu == null) {
            cliMenu = new CliMenu(dbFile);
        }
        return cliMenu;
    }

    public void mainMenu() {
        while (true) {
            System.out.println("" +
                    "1. Log in as a manager\n" +
                    "2. Log in as a customer\n" +
                    "3. Create a customer\n" +
                    "0. Exit");
            int a = enterAns();
            addWhitespace();
            if (a == 1) {
                logAsManager();
            } else if (a == 2) {
                chooseCustomer();
            } else if (a == 3) {
                createType("customer");
            } else if (a == 0) {
                break;
            }
        }
        utilsDB.closeConnect();
    }

    private void createType(String type) {
        createType(type, null);
    }

    private void createType(String type, Company company) {
        System.out.printf("Enter the %s name:", type);
        addWhitespace();
        String name = sc.nextLine();
        utilsDB.createTable(name, type, company);
        addWhitespace();
    }

    private void logAsManager() {
        while (true) {
            System.out.println("" +
                    "1. Company list\n" +
                    "2. Create a company");
            printBack();

            int a = enterAns();
            addWhitespace();
            if (a == 1) {
                showCompanies();
            } else if (a == 2) {
                createType("company");
            } else if (a == 0) {
                break;
            }
        }
    }

    private void showCompanies() {
        List<Company> listCompany = utilsDB.getAllCompanies();
        if (listCompany.isEmpty()) {
            listEmpty("company");
            addWhitespace();
        } else {
            System.out.println("Choose the company:");
            IntStream.range(0, listCompany.size())
                    .mapToObj(i -> String.format("%d. %s", i + 1, listCompany.get(i).getName()))
                    .forEach(System.out::println);
            printBack();

            int a = enterAns();
            addWhitespace();
            if (a != 0) {
                showCompanyTable(listCompany.get(a - 1));
            }
        }
    }

    private void showCompanyTable(Company company) {
        while (true) {
            String companyName = company.getName();
            System.out.printf("'%s' company", companyName);
            System.out.println("\n" +
                    "1. Car list\n" +
                    "2. Create a car");
            printBack();

            int a = enterAns();
            addWhitespace();
            if (a == 1) {
                showCars(company, false);
                addWhitespace();
            } else if (a == 2) {
                createType("car", company);
            } else if (a == 0) {
                break;
            }
        }
    }

    private List<Car> showCars(Company company, boolean isFiltered) {
        List<Car> listCars = utilsDB.getAllCar(company, isFiltered);
        if (listCars.isEmpty()) {
            listEmpty("car");
        } else {
            System.out.println("Car list:");
            IntStream.range(0, listCars.size())
                    .mapToObj(i -> String.format("%d. %s", i + 1, listCars.get(i).getName()))
                    .forEach(System.out::println);

        }
        return listCars;
    }

    private void chooseCustomer() {
        List<Customer> customerList = utilsDB.getAllCustomers();
        if (customerList.isEmpty()) {
            listEmpty("customer");
            addWhitespace();
        } else {
            System.out.println("Customer list:");
            IntStream.range(0, customerList.size())
                    .mapToObj(i -> String.format("%d. %s", i + 1, customerList.get(i).getName()))
                    .forEach(System.out::println);
            printBack();

            int a = enterAns();
            addWhitespace();
            if (a != 0) {
                showCustomerTable(customerList.get(a - 1));
            }
        }
    }

    private void showCustomerTable(Customer customer) {
        while (true) {
            System.out.println("" +
                    "1. Rent a car\n" +
                    "2. Return a rented car\n" +
                    "3. My rented car");
            printBack();

            int a = enterAns();
            addWhitespace();

            if (a == 1) {
                rentCar(customer);
            } else if (a == 2) {
                if (!utilsDB.returnCar(customer)) {
                    System.out.println("You didn't rent a car!\n");
                }
            } else if (a == 3) {
                Car car = utilsDB.getCar(customer.getRentedCarId());
                if (car == null) {
                    System.out.println("You didn't rent a car!\n");
                } else {
                    utilsDB.getCompany(car.getCompanyId());
                    System.out.println("" +
                            "Your rented car:\n" +
                            car.getName() +
                            "\nCompany:\n" +
                            utilsDB.getCompany(car.getCompanyId()).getName() +
                            "\n");
                }
            } else if (a == 0) {
                break;
            }
        }
    }

    private void rentCar(Customer customer) {
        if (customer.getRentedCarId() != 0) {
            System.out.println("You've already rented a car!");
            addWhitespace();
            return;
        }
        List<Company> listCompany = utilsDB.getAllCompanies();
        if (listCompany.isEmpty()) {
            listEmpty("company");
            addWhitespace();
        } else {
            System.out.println("Choose a company:");
            IntStream.range(0, listCompany.size())
                    .mapToObj(i -> String.format("%d. %s", i + 1, listCompany.get(i).getName()))
                    .forEach(System.out::println);
            printBack();

            int a = enterAns();
            addWhitespace();
            if (a != 0) {
                showCompanyCarTable(listCompany.get(a - 1), customer);
            }
        }
    }

    private void showCompanyCarTable(Company company, Customer customer) {
        List<Car> listCar = showCars(company, true);
        if (listCar.isEmpty()) {
            addWhitespace();
            return;
        }
        int a = enterAns();
        if (a != 0) {
            addWhitespace();
            Car car = listCar.get(a - 1);
            utilsDB.rentCar(car, customer);
            customer.setRentedCarId(car.getId());
            System.out.printf("You rented '%s'\n", car.getName());
            addWhitespace();
        }
    }


    private int enterAns() {
        String s = sc.nextLine();
        while ("".equals(s)) {
            s = sc.nextLine();
        }
        return Integer.parseInt(s.trim());
    }

    private void listEmpty(String type) {
        System.out.printf("The %s list is empty!", type);
        addWhitespace();
    }

    private void addWhitespace() {
        System.out.println();
    }

    private void printBack() {
        System.out.println("0. Back");
    }
}
