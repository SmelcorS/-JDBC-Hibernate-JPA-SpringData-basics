package jobapp.web.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter({"/faces/jsf/index.xhtml", "/faces/jsf/login.xhtml", "/faces/jsf/register.xhtml"})
public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String id = (String) req.getSession().getAttribute("username");

        if (id != null) {
            resp.sendRedirect("home.xhtml");
            return;
        }

        chain.doFilter(request, response);
    }
}
