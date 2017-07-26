package me.frostythedev.privatecontainers.storage;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.*;

public class MySQL {

    private JavaPlugin plugin;
    private Connection connection;

    public MySQL(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public boolean hasConnection() {
        try {
            return (getConnection() != null) && (!getConnection().isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean openConnection() throws SQLException{

        if (hasConnection()) {
            return true;
        }

        if (!hasConnection()) {

            if(!new File(plugin.getDataFolder(), "config.yml").exists()){
                plugin.saveDefaultConfig();
            }

            if (!plugin.getConfig().isConfigurationSection("data.mysql")) {
                plugin.getConfig().createSection("data.mysql");
                plugin.getConfig().set("data.mysql.host", "localhost");
                plugin.getConfig().set("data.mysql.port", "3306");
                plugin.getConfig().set("data.mysql.database", "test_db");
                plugin.getConfig().set("data.mysql.username", "root");
                plugin.getConfig().set("data.mysql.password", "password");
                plugin.saveConfig();
                return false;
            }

            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.err.println("jdbc driver unavailable!");
                return false;
            }
            String host = plugin.getConfig().getString("data.mysql.host");
            String port = plugin.getConfig().getString("data.mysql.port");
            String database = plugin.getConfig().getString("data.mysql.database");
            String username = plugin.getConfig().getString("data.mysql.username");
            String password = plugin.getConfig().getString("data.mysql.password");

            String url = "jdbc:mysql://" + host + ":" + port + "/" + database;

            connection = DriverManager.getConnection(url, username, password);
            return true;
        }
        return false;
    }

    public void closeConnection() throws SQLException{
        if (hasConnection()) {
            this.connection.close();
            this.connection = null;
        }
    }

    public boolean syncUpdate(String query) {
        Statement stmt = null;
        try {
            stmt = getConnection().createStatement();
            return stmt.executeUpdate(query) != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public ResultSet syncQuery(String query) {
        try {
            Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs != null) {
                return rs;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
