package carsharing;

import carsharing.cli.CliMenu;

import java.io.File;

public class Main {

    static final String DB_LOCATION = "." + File.separator + "src" + File.separator + "carsharing" + File.separator + "db";

    public static void main(String[] args) {
        String nameDB = "carsharing";

        if (args.length > 1 && "-databaseFileName".equals(args[0])) {
            nameDB = args[1];
        }
        String dbFile = DB_LOCATION + File.separator + nameDB;
        CliMenu.getInstance(dbFile).mainMenu();
    }

}