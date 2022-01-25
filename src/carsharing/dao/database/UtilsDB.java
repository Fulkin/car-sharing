package carsharing.dao.database;

import carsharing.dao.CompanyDAO;
import carsharing.dao.CompanyDAOImpl;
import carsharing.dao.model.Car;
import carsharing.dao.model.Company;
import carsharing.dao.model.Customer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author Fulkin
 * Created on 21.01.2022
 */

public class UtilsDB {
    static final String JDBC_DRIVER = "org.h2.Driver";
    private static UtilsDB utilsDB = null;

    private CompanyDAO companyDAO;
    private Connection conn;
    private Statement st;

    private UtilsDB() {
    }

    public static UtilsDB getInstance() {
        if (utilsDB == null) {
            utilsDB = new UtilsDB();
        }
        return utilsDB;
    }

    public void connectToDB(String dbFile) {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection("jdbc:h2:" + dbFile);
            st = conn.createStatement();
            initTable();
            companyDAO = new CompanyDAOImpl(st);
            conn.setAutoCommit(true);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void createTable(String name, String type, Company company) {
        try {
            if (company != null) {
                companyDAO.createCar(name, company.getId());
            } else if ("company".equals(type)) {
                companyDAO.create(name);
            } else if ("customer".equals(type)) {
                companyDAO.createCustomer(name);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<Company> getAllCompanies() {
        List<Company> companyList = null;
        try {
            companyList = companyDAO.getAll();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return companyList;
    }

    public List<Car> getAllCar(Company company, boolean isFiltered) {
        List<Car> carList = null;
        try {
            carList = companyDAO.getAllCar(company, isFiltered);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return carList;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customerList = null;
        try {
            customerList = companyDAO.getAllCustomers();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customerList;
    }

    public void rentCar(Car car, Customer customer) {
        try {
            companyDAO.updateCustomerCar(
                    new int[]{car.getId(), customer.getId()}
            );

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean returnCar(Customer customer) {
        try {
            if (customer.getRentedCarId() == 0) {
                return false;
            }
            companyDAO.deleteCustomerCar(customer);
            customer.setRentedCarId(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public Company getCompany(int companyId) {
        Company company = null;
        try {
            company = companyDAO.getCompany(companyId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return company;
    }

    public Car getCar(int rentedCarId) {
        Car car = null;
        try {
            car = companyDAO.getCar(rentedCarId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return car;
    }


    public void closeConnect() {
        try {
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initTable() throws SQLException {
        st.executeUpdate("DROP TABLE IF EXISTS customer;");
        st.executeUpdate("DROP TABLE IF EXISTS car;");
        st.executeUpdate("DROP TABLE IF EXISTS company;");
        st.executeUpdate("CREATE TABLE IF NOT EXISTS company(" +
                "id INT AUTO_INCREMENT," +
                "name VARCHAR(255) NOT NULL UNIQUE," +
                "PRIMARY KEY (id)" +
                ");");
        st.executeUpdate("CREATE TABLE IF NOT EXISTS car(" +
                "id INT AUTO_INCREMENT," +
                "name VARCHAR(255) NOT NULL UNIQUE," +
                "company_id INT NOT NULL," +
                "FOREIGN KEY (company_id) REFERENCES company(id)," +
                "PRIMARY KEY (id)" +
                ");");
        st.executeUpdate("CREATE TABLE IF NOT EXISTS customer(" +
                "id INT AUTO_INCREMENT," +
                "name VARCHAR(255) NOT NULL UNIQUE," +
                "rented_car_id INT," +
                "FOREIGN KEY (rented_car_id) REFERENCES car(id)," +
                "PRIMARY KEY (id)" +
                ");");
    }
}
