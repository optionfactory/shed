package net.optionfactory.shed;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class ParameterQuotingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
       
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(new QuotingRequestDecorator((HttpServletRequest) request), response);
    }

    @Override
    public void destroy() {
        
    }
    
    public static class QuotingRequestDecorator extends HttpServletRequestWrapper {

        public QuotingRequestDecorator(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getParameter(String name) {
            return String.format("\"%s\"", super.getParameter(name));
        }
        
        
    }
    
}
