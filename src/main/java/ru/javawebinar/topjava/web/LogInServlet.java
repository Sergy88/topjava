package ru.javawebinar.topjava.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogInServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("loginId", SecurityUtil.authUserId());
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String param = req.getParameter("login");
        int userId = Integer.parseInt(req.getParameter("login"));
        SecurityUtil.setLoggedUser(userId);
        req.setAttribute("loginId", SecurityUtil.authUserId());
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}
