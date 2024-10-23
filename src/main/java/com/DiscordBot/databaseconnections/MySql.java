package com.DiscordBot.databaseconnections;

import java.io.IOException;
import java.sql.*;

public class MySql {

    private static Connection conn;
    private static PreparedStatement statement;

    public static void connect(String user, String password) throws IOException, SQLException {

        String connectionUrl = "jdbc:mysql://localhost:3306/Discord_bot";

        try {
            conn = DriverManager.getConnection(connectionUrl, user, password);
            System.out.println("Datenbankverbindung hergestellt");
        } catch (SQLException e) {
            System.out.println("Fehler beim Verbinden mit der Datenbank " + e);
        }
    }

    public static void disconnect() throws SQLException {
        if (conn != null) {
            conn.close();
            System.out.println("Datenbankverbindung geschlossen");
        }
    }

    public static void update(String sqlQuery) throws SQLException {
        try {
            statement = conn.prepareStatement(sqlQuery);
            statement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Fehler beim ausführen der Query " + e);
        }


    }

    public static ResultSet query(String sqlQuery) throws SQLException {
        ResultSet result = null;
        try {
            statement = conn.prepareStatement(sqlQuery);
            result = statement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Fehler beim ausführen der Query " + e);
        }
        return result;
    }
}

