package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.entities.Product;
import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.html.HtmlBuilder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {
    private final ProductDao dao = new ProductDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                var result = dao.getAll();
                var writer = new HtmlBuilder();

                for (Product p : result) {
                    String name = p.getName();
                    int price  = p.getPrice();
                    writer.brLine(name + "\t" + price);
                }
                response.getWriter().println(writer.toString());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
