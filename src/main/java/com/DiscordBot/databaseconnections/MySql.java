package com.DiscordBot.databaseconnections;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySql {

    private static Connection conn;
    private static PreparedStatement statement;


    // Utilty DataBase Methods

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

    // Insert Update and get Roles of users

    public static void insertIntoRoleDB(String user, String role) throws SQLException {

        try {
            statement = conn.prepareStatement("INSERT INTO roles (User_ID, Role_ID) VALUES (?, ?)");
            statement.setString(1, user);
            statement.setString(2, role);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Fehler beim ausführen der Query " + e);
        }
    }

    public static List<String> getUserRole(String user) throws SQLException {
        ResultSet result = null;
        List<String> resultList = new ArrayList<String>();
        try {
            statement = conn.prepareStatement("SELECT Role_ID FROM roles WEHRE User_ID = ?");
            statement.setString(1, user);
            result = statement.executeQuery();

            while (result.next()) {
                resultList.add(result.getString("User_ID"));
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim ausführen der Query " + e);
        }
        return resultList;
    }

}



