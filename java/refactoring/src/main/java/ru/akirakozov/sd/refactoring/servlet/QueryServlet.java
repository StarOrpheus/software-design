package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dao.ProductDao;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    private final ProductDao dao = new ProductDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        try {
            if ("max".equals(command)) {
                var result = dao.getMaxByPrice();
                response.getWriter().println("<html><body>");
                response.getWriter().println("<h1>Product with max price: </h1>");
                if (result != null) {
                    var name = result.getName();
                    var price  = result.getPrice();
                    response.getWriter().println(name + "\t" + price + "</br>");
                }
                response.getWriter().println("</body></html>");
            } else if ("min".equals(command)) {
                var result = dao.getMinByPrice();
                response.getWriter().println("<html><body>");
                response.getWriter().println("<h1>Product with min price: </h1>");
                if (result != null) {
                    var name = result.getName();
                    int price  = result.getPrice();
                    response.getWriter().println(name + "\t" + price + "</br>");
                }
                response.getWriter().println("</body></html>");
            } else if ("sum".equals(command)) {
                var result = dao.getSumPrice();
                response.getWriter().println("<html><body>");
                response.getWriter().println("Summary price: ");
                response.getWriter().println(Long.valueOf(result).toString());
                response.getWriter().println("</body></html>");
            } else if ("count".equals(command)) {
                var result = dao.getCount();
                response.getWriter().println("<html><body>");
                response.getWriter().println("Number of products: ");
                response.getWriter().println(Long.valueOf(result).toString());
                response.getWriter().println("</body></html>");
            } else {
                response.getWriter().println("Unknown command: " + command);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
