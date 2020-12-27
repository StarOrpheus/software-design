package ru.akirakozov.sd.refactoring.dao;

import ru.akirakozov.sd.refactoring.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ProductDao {
    public void addProduct(String name, long price) throws SQLException {
        Connection c = DriverManager.getConnection("jdbc:sqlite:test.db");
        String sql = "INSERT INTO PRODUCT " +
                "(NAME, PRICE) VALUES (\"" + name + "\"," + price + ")";
        Statement stmt = c.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
    }

    public List<Product> getAll() throws SQLException {
        return executeQuery("SELECT * FROM PRODUCT", EXTRACT_ENTITIES);
    }

    public long getCount() throws SQLException {
        return executeQuery("SELECT COUNT(*) FROM PRODUCT", EXTRACT_LONG);
    }

    public long getSumPrice() throws SQLException {
        return executeQuery("SELECT SUM(price) FROM PRODUCT", EXTRACT_LONG);
    }

    public Product getMaxByPrice() throws SQLException {
        return executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1", EXTRACT_ENTITIES)
                .stream()
                .findFirst()
                .orElse(null);
    }

    public Product getMinByPrice() throws SQLException {
        return executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1", EXTRACT_ENTITIES)
                .stream()
                .findFirst()
                .orElse(null);
    }

    private <R> R executeQuery(String query, Function<ResultSet, R> mapper) throws SQLException {
        Connection c = DriverManager.getConnection("jdbc:sqlite:test.db");
        try (Statement stmt = c.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(query)) {
                return mapper.apply(rs);
            }
        }
    }

    private final Function<ResultSet, List<Product>> EXTRACT_ENTITIES = (rs) -> {
        List<Product> result = new ArrayList<>();
        while (true) {
            try {
                if (!rs.next()) break;
            } catch (SQLException throwables) {
                break;
            }

            try {
                result.add(new Product(rs.getString(Product.NAME), rs.getInt(Product.PRICE), rs.getLong(Product.ID)));
            } catch (SQLException ignored) { }
        }
        return result;
    };

    private final Function<ResultSet, Long> EXTRACT_LONG = (rs) -> {
        try {
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                return 0L;
            }
        } catch (SQLException ignored) {
            return 0L;
        }
    };
}
