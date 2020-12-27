package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.html.HtmlBuilder;

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
        var command = request.getParameter("command");
        var writer = new HtmlBuilder();

        try {
            if ("max".equals(command)) {
                var result = dao.getMaxByPrice();
                writer.h1Header("Product with max price: ");
                if (result != null) {
                    var name = result.getName();
                    var price  = result.getPrice();
                    writer.brLine(name + "\t" + price);
                }
            } else if ("min".equals(command)) {
                var result = dao.getMinByPrice();
                writer.h1Header("Product with min price: ");
                if (result != null) {
                    var name = result.getName();
                    int price  = result.getPrice();
                    writer.brLine(name + "\t" + price);
                }
            } else if ("sum".equals(command)) {
                var result = dao.getSumPrice();
                writer.line("Summary price: ");
                writer.line(Long.valueOf(result).toString());
            } else if ("count".equals(command)) {
                var result = dao.getCount();
                writer.line("Number of products: ");
                writer.line(Long.valueOf(result).toString());
            } else {
                writer.line("Unknown command: " + command);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(writer.toString());
    }

}
