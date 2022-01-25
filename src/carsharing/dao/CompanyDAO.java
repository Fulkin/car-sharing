package carsharing.dao;

import carsharing.dao.model.Car;
import carsharing.dao.model.Company;
import carsharing.dao.model.Customer;

import java.sql.SQLException;
import java.util.List;

public interface CompanyDAO {
    void create(String name) throws SQLException;

    List<Company> getAll() throws SQLException;

    void createCar(String name, int idCompany) throws SQLException;

    List<Car> getAllCar(Company company, boolean isFiltered) throws SQLException;

    void createCustomer(String name) throws SQLException;

    List<Customer> getAllCustomers() throws SQLException;

    void updateCustomerCar(int[] a) throws SQLException;

    void deleteCustomerCar(Customer customer) throws SQLException;

    Company getCompany(int id) throws SQLException;

    Car getCar(int id) throws SQLException;
}
