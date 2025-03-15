package org.example.Servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.Utilities;
import org.example.dataBase.UserDao;
import org.example.entities.User;
import org.example.services.VerificationEmail;
import java.io.IOException;

public class UserAuthServlet extends HttpServlet {
    VerificationEmail mail = new VerificationEmail();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(true);
        String step = req.getParameter("step");  // This determines the action

        if ("sendEmail".equals(step)) {
            // ✅ Step 1: Send Verification Email
            String userName = req.getParameter("userName");
            String userEmail = req.getParameter("userEmail");

            session.setAttribute("userName", userName);
            session.setAttribute("userEmail", userEmail);

            if (mail.isEmailAvailable(userEmail)) {
                if (mail.sendEmail(userEmail, userName)) {
                    resp.getWriter().write("{\"success\": true, \"message\": \"Email sent successfully. Enter verification code.\"}");
                } else {
                    resp.getWriter().write("{\"success\": false, \"message\": \"Error sending email. Try again later.\"}");
                }
            } else {
                resp.getWriter().write("{\"success\": false, \"message\": \"Email already registered.\"}");
            }

        } else if ("verifyCode".equals(step)) {
            // ✅ Step 2: Verify OTP
            int userInputCode = Integer.parseInt(req.getParameter("InputCode"));

            if (mail.authenticator(userInputCode)) {
                session.setAttribute("emailVerified", true);
                resp.getWriter().write("{\"success\": true, \"message\": \"Email verified. Enter your password.\"}");
            } else {
                resp.getWriter().write("{\"success\": false, \"message\": \"Invalid verification code.\"}");
            }

        } else if ("setPassword".equals(step)) {
            // ✅ Step 3: Set Password
            Boolean isVerified = (Boolean) session.getAttribute("emailVerified");

            if (isVerified == null || !isVerified) {
                resp.getWriter().write("{\"success\": false, \"message\": \"Email verification required.\"}");
                return;
            }

            String password = req.getParameter("password");
            session.setAttribute("password", password);

            resp.getWriter().write("{\"success\": true, \"message\": \"Password set successfully. Signup complete!\"}");

            //creating a new user
            String userName = (String) session.getAttribute("userName");
            String userEmail = (String) session.getAttribute("userEmail");
            String userPassword = (String) session.getAttribute("password");
            String hashedPassword =  Utilities.passwordEncryptor(userPassword);
            UserDao userDao = new UserDao();
            User user = new User(userName, userEmail, hashedPassword);
            userDao.registerUser(user);
        }
    }
}
