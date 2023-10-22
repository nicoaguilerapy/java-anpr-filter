package controller;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import org.json.JSONObject;
import org.json.JSONTokener;

public class DB {

    private static Connection connection;
    private static JSONObject config;
    private static ResultSet stmt;

    public static void connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            // Leer los datos de conexión desde un archivo JSON
            config = readConfigFromJSON("config.json");

            if (config != null) {
                String host = (String) config.get("host");
                String db = (String) config.get("db");
                int port = Integer.parseInt((String) config.get("port"));

                try {
                    String connectionString = "jdbc:postgresql://" + host + ":" + port + "/" + db;
                connection = DriverManager.getConnection(connectionString, "postgres", "123123");
                    System.out.println("controller.PostgresSingleton.connect(): CONECTADO CORRECTAMENTE");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean executeUpdate(String query) {
        try {
            connect();
            return connection.createStatement().executeUpdate(query) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void executeQuery(String query) {
        try {
            connect();
            System.out.println("controller.DB.executeQuery()>>> "+query);
            stmt = connection.createStatement().executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static JSONObject readConfigFromJSON(String fileName) {
        try {
            FileReader reader = new FileReader(fileName);
            JSONTokener tokener = new JSONTokener(reader);
            return new JSONObject(tokener);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ResultSet getResulset() {
        return stmt;
    }
}
