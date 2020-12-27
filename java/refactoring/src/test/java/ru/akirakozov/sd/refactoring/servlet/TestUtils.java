package ru.akirakozov.sd.refactoring.servlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TestUtils {
    static void setupProductTable(String url) throws SQLException {
        Connection c = DriverManager.getConnection(url);
        try (Statement stmt = c.createStatement()) {
            stmt.executeUpdate("DROP TABLE IF EXISTS PRODUCT");
        }

        try (Statement stmt = c.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            stmt.executeUpdate(sql);
        }
    }
}
