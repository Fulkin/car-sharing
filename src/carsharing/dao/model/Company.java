package carsharing.dao.model;

/**
 * @author Fulkin
 * Created on 21.01.2022
 */

public class Company {
    private int id;
    private String name;

    public Company(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
