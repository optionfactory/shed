package net.optionfactory.minispring.config;

import net.optionfactory.minispring.tiles.TilesConfig;
import net.optionfactory.minispring.pages.PagesConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class WebConfigurer implements WebApplicationInitializer {



    @Override
    public void onStartup(ServletContext sc) throws ServletException {
        // sc.addListener(RequestContextListener.class);
        // sc.addFilter("springSecurityFilterChain", new DelegatingFilterProxy()).addMappingForUrlPatterns(null, false, "/*");

        final AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(DbConfig.class, CoreConfig.class);
        sc.addListener(new ContextLoaderListener(rootContext));

        final AnnotationConfigWebApplicationContext apiContext = new AnnotationConfigWebApplicationContext();
        apiContext.register(ApiConfig.class);
        final ServletRegistration.Dynamic api = sc.addServlet("api", new DispatcherServlet(apiContext));
        api.setLoadOnStartup(1);
        api.addMapping("/api/*");

        final AnnotationConfigWebApplicationContext jspsContext = new AnnotationConfigWebApplicationContext();
        jspsContext.register(PagesConfig.class);
        final ServletRegistration.Dynamic jsps = sc.addServlet("jsps", new DispatcherServlet(jspsContext));
        jsps.setLoadOnStartup(1);
        jsps.addMapping("/pages/*");

        final AnnotationConfigWebApplicationContext tilesContext = new AnnotationConfigWebApplicationContext();
        tilesContext.register(TilesConfig.class);
        final ServletRegistration.Dynamic tiles = sc.addServlet("tiles", new DispatcherServlet(tilesContext));
        tiles.setLoadOnStartup(1);
        tiles.addMapping("/tiles/*");
    }
}
