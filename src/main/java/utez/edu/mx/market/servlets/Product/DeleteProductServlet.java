package utez.edu.mx.market.servlets.Product;

import utez.edu.mx.market.daos.DaoProduct;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "DeleteProductServlet", value = "/DeleteProductServlet")
public class DeleteProductServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/view/home.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset-UTF-8");

        DaoProduct dao = new DaoProduct();
        long id = Long.parseLong(request.getParameter("id"));

        request.setAttribute("success", dao.deleteProduct(id));
        doGet(request,response);
    }
}