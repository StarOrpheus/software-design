package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class QueryServletTest {
    private final String DB_URL = "jdbc:sqlite:test.db";
    private final StringWriter strWriter = new StringWriter();
    private final PrintWriter writer = new PrintWriter(strWriter);
    private final QueryServlet servlet = new QueryServlet();

    private final HttpServletRequest mockedRequest = Mockito.mock(HttpServletRequest.class);
    private final HttpServletResponse mockedResponse = Mockito.mock(HttpServletResponse.class);

    private enum ResponseFormat {
        MAX_HEADER, MIN_HEADER, SUM_HEADER, COUNT_HEADER
    }

    @BeforeEach
    public void setup() throws SQLException, IOException {
        TestUtils.setupProductTable(DB_URL);
        when(mockedResponse.getWriter()).thenReturn(writer);
    }

    @Test
    void basicMaxOnEmpty() throws IOException {
        when(mockedRequest.getParameter("command")).thenReturn("max");
        servlet.doGet(mockedRequest, mockedResponse);
        assertEquals(format(ResponseFormat.MAX_HEADER, ""), strWriter.toString());
    }

    @Test
    void basicMaxOnSome() throws IOException, SQLException {
        setupSomeProducts();
        when(mockedRequest.getParameter("command")).thenReturn("max");
        servlet.doGet(mockedRequest, mockedResponse);
        assertEquals(format(ResponseFormat.MAX_HEADER, "u menya\t331</br>\n"), strWriter.toString());
    }

    @Test
    void basicMinOnEmpty() throws IOException {
        when(mockedRequest.getParameter("command")).thenReturn("min");
        servlet.doGet(mockedRequest, mockedResponse);
        assertEquals(format(ResponseFormat.MIN_HEADER, ""), strWriter.toString());
    }

    @Test
    void basicMinOnSome() throws IOException, SQLException {
        setupSomeProducts();
        when(mockedRequest.getParameter("command")).thenReturn("min");
        servlet.doGet(mockedRequest, mockedResponse);
        assertEquals(format(ResponseFormat.MIN_HEADER, "otvalilas\t42</br>\n"), strWriter.toString());
    }

    @Test
    void basicSumOnEmpty() throws IOException {
        when(mockedRequest.getParameter("command")).thenReturn("sum");
        servlet.doGet(mockedRequest, mockedResponse);
        assertEquals(format(ResponseFormat.SUM_HEADER, "0\n"), strWriter.toString());
    }

    @Test
    void basicSumOnSome() throws IOException, SQLException {
        setupSomeProducts();
        when(mockedRequest.getParameter("command")).thenReturn("sum");
        servlet.doGet(mockedRequest, mockedResponse);
        assertEquals(format(ResponseFormat.SUM_HEADER, "601\n"), strWriter.toString());
    }

    @Test
    void basicCountOnEmpty() throws IOException {
        when(mockedRequest.getParameter("command")).thenReturn("count");
        servlet.doGet(mockedRequest, mockedResponse);
        assertEquals(format(ResponseFormat.COUNT_HEADER, "0\n"), strWriter.toString());
    }

    @Test
    void basicCountOnSome() throws IOException, SQLException {
        setupSomeProducts();
        when(mockedRequest.getParameter("command")).thenReturn("count");
        servlet.doGet(mockedRequest, mockedResponse);
        assertEquals(format(ResponseFormat.COUNT_HEADER, "3\n"), strWriter.toString());
    }

    private String format(ResponseFormat format, String result) {
        switch (format) {
            case MAX_HEADER:
                return "<html><body>\n<h1>Product with max price: </h1>\n" + result + "</body></html>\n";
            case MIN_HEADER:
                return "<html><body>\n<h1>Product with min price: </h1>\n" + result + "</body></html>\n";
            case SUM_HEADER:
                return "<html><body>\nSummary price: \n" + result + "</body></html>\n";
            case COUNT_HEADER:
                return "<html><body>\nNumber of products: \n" + result + "</body></html>\n";

        }
        return null;
    }

    private void setupSomeProducts() throws SQLException {
        String sql = "INSERT INTO PRODUCT (NAME, PRICE) VALUES ('jopa', 228), ('otvalilas', 42), ('u menya', 331)";
        Connection c = DriverManager.getConnection(DB_URL);
        try (Statement stmt = c.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
}
