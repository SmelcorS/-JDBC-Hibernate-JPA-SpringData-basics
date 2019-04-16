package jobapp.web.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter({"/faces/jsf/home.xhtml", "/faces/jsf/job-details.xhtml", "/faces/jsf/delete-job.xhtml", "/faces/jsf/add-job.xhtml"})
public class GuestFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String id = (String) req.getSession().getAttribute("username");

        if (id == null) {
            resp.sendRedirect("login.xhtml");
            return;
        }

        chain.doFilter(request, response);

    }
}
