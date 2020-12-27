package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class AddProductServletTest {
    private final StringWriter strWriter = new StringWriter();
    private final PrintWriter writer = new PrintWriter(strWriter);
    private final AddProductServlet servlet = new AddProductServlet();

    private final HttpServletRequest mockedRequest = Mockito.mock(HttpServletRequest.class);
    private final HttpServletResponse mockedResponse = Mockito.mock(HttpServletResponse.class);

    @BeforeEach
    public void setup() throws SQLException, IOException {
        String DB_URL = "jdbc:sqlite:test.db";
        TestUtils.setupProductTable(DB_URL);
        when(mockedResponse.getWriter()).thenReturn(writer);
    }

    /**
     * Basic product addition test
     */
    @Test
    public void simpleAdd() throws IOException {
        when(mockedRequest.getParameter("name")).thenReturn("expensive toilet paper");
        when(mockedRequest.getParameter("price")).thenReturn("42");

        servlet.doGet(mockedRequest, mockedResponse);

        assertEquals("OK\n", strWriter.toString());
    }

    /**
     * Basic product addition with nullable Name param
     */
    @Test
    public void simpleAdd2() throws IOException {
        when(mockedRequest.getParameter("name")).thenReturn(null);
        when(mockedRequest.getParameter("price")).thenReturn("42");

        servlet.doGet(mockedRequest, mockedResponse);

        assertEquals("OK\n", strWriter.toString());
    }

    /**
     * Basic product addition with nullable Price param
     */
    @Test
    public void simpleAdd3() {
        when(mockedRequest.getParameter("name")).thenReturn("nameme");
        when(mockedRequest.getParameter("price")).thenReturn(null);

        assertThrows(java.lang.NumberFormatException.class,
                () -> servlet.doGet(mockedRequest, mockedResponse));
    }
}
