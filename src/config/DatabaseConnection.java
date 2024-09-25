package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection = null;

    public DatabaseConnection() {
    }


    public static Connection getConnection() {
        if (connection == null) {
            synchronized (DatabaseConnection.class) {
                if (connection == null) {
                    try {
                        // Charger le driver manuellement
                        Class.forName("org.postgresql.Driver");

                        // Établir la connexion
                        connection = DriverManager.getConnection(
                                "jdbc:postgresql://localhost:5432/BatiCuisine",
                                "BatiCuisine",
                                "your_password"
                        );
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        System.out.println("Le driver PostgreSQL n'a pas pu être chargé.");
                        e.printStackTrace();
                    }
                }
            }
        }
        return connection;
    }


}
