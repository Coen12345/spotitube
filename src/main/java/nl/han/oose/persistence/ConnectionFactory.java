package nl.han.oose.persistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private static final String MYSQL_JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/spotitube?serverTimezone=UTC";
    private static final String DB_USER = "coen";
    private static final String DB_PASSWORD = "lakerol";

    private Properties properties;

    public ConnectionFactory() {
        properties = getProperties();
    }

    private Properties getProperties() {
        Properties properties = new Properties();
        String propertiesPath = getClass().
                getClassLoader()
                .getResource("")
                .getPath() + "database.properties";
        try {
            FileInputStream fileInputStream = new FileInputStream(propertiesPath);
            properties.load(fileInputStream);
        } catch (IOException e) {
            properties.setProperty("db.url", CONNECTION_URL);
            properties.setProperty("db.driver", MYSQL_JDBC_DRIVER);
            properties.setProperty("db.user", DB_USER);
            properties.setProperty("db.pass", DB_PASSWORD);
            e.printStackTrace();
        }
        return properties;
    }

    public Connection getConnection() {
        loadDriver();
        try {
            return DriverManager.getConnection(properties.getProperty("db.url"),
                    properties.getProperty("db.user"),
                    properties.getProperty("db.pass"));
        } catch (SQLException e) {
            throw new SpotitubePersistenceException(e);
        }
    }

    private void loadDriver() {
        try {
            Class.forName(properties.getProperty("db.driver"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
