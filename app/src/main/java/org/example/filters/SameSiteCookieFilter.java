package org.example.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class SameSiteCookieFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(req, res);
        if (req instanceof HttpServletRequest && res instanceof HttpServletResponse) {
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) res;
            HttpSession session = request.getSession(false);
            if (session != null) {
                String sessionId = session.getId();
                String contextPath = request.getContextPath();
                if (contextPath == null || contextPath.isEmpty()) {
                    contextPath = "/";
                }
                String cookie = "JSESSIONID=" + sessionId +
                        "; Path=" + contextPath +
                        "; HttpOnly; Secure; SameSite=None; Max-Age=2592000"; // 30 days
                response.addHeader("Set-Cookie", cookie);
            }
        }
    }
}