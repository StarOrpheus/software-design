package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class GetProductServletTest {
    private final String DB_URL = "jdbc:sqlite:test.db";
    private final StringWriter strWriter = new StringWriter();
    private final PrintWriter writer = new PrintWriter(strWriter);
    private final GetProductsServlet servlet = new GetProductsServlet();

    private final HttpServletRequest mockedRequest = Mockito.mock(HttpServletRequest.class);
    private final HttpServletResponse mockedResponse = Mockito.mock(HttpServletResponse.class);

    @BeforeEach
    public void setup() throws SQLException, IOException {
        TestUtils.setupProductTable(DB_URL);
        when(mockedResponse.getWriter()).thenReturn(writer);
    }

    @Test
    void testEmptyGet() throws IOException {
        servlet.doGet(mockedRequest, mockedResponse);

        assertEquals("<html><body>\n</body></html>\n", strWriter.toString());
    }

    @Test
    void testGetAll() throws IOException, SQLException {
        String sql = "INSERT INTO PRODUCT (NAME, PRICE) VALUES ('jopa', 228), ('otvalilas', 42), ('u menya', 331)";
        Connection c = DriverManager.getConnection(DB_URL);
        try (Statement stmt = c.createStatement()) {
            stmt.executeUpdate(sql);
        }

        servlet.doGet(mockedRequest, mockedResponse);

        assertEquals(
                "<html><body>\njopa\t228</br>\notvalilas\t42</br>\nu menya\t331</br>\n</body></html>\n",
                strWriter.toString());
    }
}
