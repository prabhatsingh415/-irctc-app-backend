package org.example.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.utilities.Utilities;
import org.example.database.UserDao;
import org.example.entities.User;
import org.example.services.VerificationEmail;
import java.io.IOException;

public class UserAuthServlet extends HttpServlet {
    VerificationEmail mail = new VerificationEmail();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(true);
        System.out.println("Step: " + req.getParameter("step") + ", Session ID: " + session.getId());
        String step = req.getParameter("step");

        if ("sendEmail".equals(step)) {
            String userName = req.getParameter("userName");
            String userEmail = req.getParameter("userEmail");

            session.setAttribute("userName", userName);
            session.setAttribute("userEmail", userEmail);

            if (mail.isEmailAvailable(userEmail)) {
                if (mail.sendEmail(userEmail, userName, session)) {
                    resp.getWriter().write("{\"success\": true, \"message\": \"Email sent successfully. Enter verification code.\"}");
                } else {
                    resp.getWriter().write("{\"success\": false, \"message\": \"Error sending email. Try again later.\"}");
                }
            } else {
                resp.getWriter().write("{\"success\": false, \"message\": \"Email already registered.\"}");
            }
        } else if ("verifyCode".equals(step)) {
            String inputCodeStr = req.getParameter("InputCode");
            System.out.println("Received InputCode: " + inputCodeStr);
            int userInputCode;
            try {
                userInputCode = Integer.parseInt(inputCodeStr);
            } catch (NumberFormatException e) {
                resp.getWriter().write("{\"success\": false, \"message\": \"Invalid verification code format.\"}");
                return;
            }

            if (mail.authenticator(userInputCode, session)) {
                session.setAttribute("emailVerified", true);
                resp.getWriter().write("{\"success\": true, \"message\": \"Email verified. Enter your password.\"}");
            } else {
                resp.getWriter().write("{\"success\": false, \"message\": \"Invalid verification code.\"}");
            }
        } else if ("setPassword".equals(step)) {
            Boolean isVerified = (Boolean) session.getAttribute("emailVerified");
            if (isVerified == null || !isVerified) {
                resp.getWriter().write("{\"success\": false, \"message\": \"Email verification required.\"}");
                return;
            }

            String password = req.getParameter("password");
            session.setAttribute("password", password);

            resp.getWriter().write("{\"success\": true, \"message\": \"Password set successfully! Signup complete.\"}");

            String userName = (String) session.getAttribute("userName");
            String userEmail = (String) session.getAttribute("userEmail");
            String userPassword = (String) session.getAttribute("password");
            String hashedPassword = Utilities.passwordEncryptor(userPassword);
            UserDao userDao = new UserDao();
            User user = new User(userName, userEmail, hashedPassword);
            userDao.registerUser(user);
        }
    }
}