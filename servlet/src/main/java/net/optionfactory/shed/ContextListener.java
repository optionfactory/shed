package net.optionfactory.shed;

import java.util.EnumSet;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {
  
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        ServletRegistration.Dynamic dynamic = sc.addServlet(
                    CheersServlet.class.getSimpleName(),
                    new CheersServlet());
        dynamic.addMapping("/cheers");
        FilterRegistration.Dynamic filter = sc.addFilter(
                    ParameterQuotingFilter.class.getSimpleName(),
                    new ParameterQuotingFilter());
        EnumSet<DispatcherType> disps = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD);
        filter.addMappingForServletNames(disps, true, CheersServlet.class.getSimpleName());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) { 
    }
}
