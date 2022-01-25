package carsharing.dao;

import carsharing.dao.model.Car;
import carsharing.dao.model.Company;
import carsharing.dao.model.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAOImpl implements CompanyDAO {
    Statement statement;

    public CompanyDAOImpl(Statement statement) {
        this.statement = statement;
    }

    @Override
    public void create(String name) throws SQLException {
        String sqlQuery = String.format("" +
                "INSERT INTO company(name) " +
                "VALUES ('%s');", name);
        statement.executeUpdate(sqlQuery);
        System.out.println("The company was created!");
    }

    @Override
    public void createCar(String name, int idCompany) throws SQLException {
        String sqlQuery = String.format("" +
                "INSERT INTO car(name, company_id) " +
                "VALUES ('%s', %d)", name, idCompany);
        statement.executeUpdate(sqlQuery);
        System.out.println("The car was added!");
    }

    @Override
    public void createCustomer(String name) throws SQLException {
        String sqlQuery = String.format("" +
                "INSERT INTO customer(name) " +
                "VALUES ('%s')", name);
        statement.executeUpdate(sqlQuery);
        System.out.println("The customer was added!");
    }

    @Override
    public List<Company> getAll() throws SQLException {
        String sqlQuery = "SELECT * FROM company;";
        List<Company> list = new ArrayList<>();
        ResultSet rs = statement.executeQuery(sqlQuery);
        while (rs.next()) {
            int idCompany = rs.getInt("id");
            String nameCompany = rs.getString("name");
            list.add(new Company(idCompany, nameCompany));
        }
        return list;
    }

    @Override
    public List<Car> getAllCar(Company company, boolean isFiltered) throws SQLException {
        List<Car> carList = new ArrayList<>();
        String sqlQuery;
        if (isFiltered) {
            sqlQuery = String.format("" +
                            "SELECT * FROM car " +
                            "WHERE company_id = %d AND" +
                            "   id NOT IN (" +
                            "   SELECT rented_car_id " +
                            "   FROM customer " +
                            "   WHERE rented_car_id IS NOT NULL)" +
                            ";",
                    company.getId());
        } else {
            sqlQuery = String.format("" +
                            "SELECT * FROM car " +
                            "WHERE company_id = %d",
                    company.getId());
        }
        ResultSet rs = statement.executeQuery(sqlQuery);

        while (rs.next()) {
            int idCar = rs.getInt("id");
            String nameCar = rs.getString("name");
            int idCompany = rs.getInt("company_id");
            carList.add(new Car(idCar, nameCar, idCompany));
        }
        return carList;
    }

    @Override
    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customerList = new ArrayList<>();

        String sqlQuery = "" +
                "SELECT id, name, rented_car_id FROM customer ";
        ResultSet rs = statement.executeQuery(sqlQuery);
        while (rs.next()) {
            int idCustomer = rs.getInt("id");
            String nameCar = rs.getString("name");
            int idRentedCar = rs.getInt("rented_car_id");
            customerList.add(new Customer(idCustomer, nameCar, idRentedCar));
        }
        return customerList;
    }

    @Override
    public Company getCompany(int id) throws SQLException {
        Company company = null;
        String sqlQuery = String.format("" +
                "SELECT id, name FROM company " +
                "WHERE id = %d", id);
        ResultSet rs = statement.executeQuery(sqlQuery);
        while (rs.next()) {
            int idCompany = rs.getInt("id");
            String nameCompany = rs.getString("name");
            company = new Company(idCompany, nameCompany);
        }
        return company;
    }

    @Override
    public Car getCar(int id) throws SQLException {
        Car car = null;
        String sqlQuery = String.format("" +
                "SELECT id, name, company_id FROM car " +
                "WHERE id = %d", id);
        ResultSet rs = statement.executeQuery(sqlQuery);
        while (rs.next()) {
            int idCar = rs.getInt("id");
            String nameCar = rs.getString("name");
            int companyId = rs.getInt("company_id");
            car = new Car(idCar, nameCar, companyId);
        }
        return car;
    }

    @Override
    public void updateCustomerCar(int[] a) throws SQLException {
        String sqlQuery = String.format("" +
                        "UPDATE customer " +
                        "SET rented_car_id = %d " +
                        "WHERE customer.id = %d",
                a[0], a[1]);
        statement.executeUpdate(sqlQuery);
    }

    @Override
    public void deleteCustomerCar(Customer customer) throws SQLException {
        String sqlQuery = String.format("" +
                        "UPDATE customer " +
                        "SET rented_car_id = NULL " +
                        "WHERE customer.id = %d",
                customer.getId());
        statement.executeUpdate(sqlQuery);
        System.out.println("You've returned a rented car!\n");
    }
}
