package org.example.Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dataBase.UserDao;


import java.io.IOException;


public class LoginServlet extends HttpServlet {

   UserDao newUser = new UserDao();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String email = req.getParameter("email");
        String password = req.getParameter("password");

       if(newUser.login(email,password)){
           resp.getWriter().write("{\"message\": \"Login successful\"}");
       } else {
           resp.getWriter().write("{\"message\": \"Invalid credentials\"}");
       }
    }
}
