package org.example;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.services.VerificationEmail;
import org.example.servlet.UserAuthServlet;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;

public class AppTest {

    public static void main(String[] args) {
        VerificationEmail mail = new VerificationEmail();
//        mail.sendEmail(" dadade8455@cristout.com","psr");
    }
}