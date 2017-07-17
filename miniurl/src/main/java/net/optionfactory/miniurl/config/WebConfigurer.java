package net.optionfactory.miniurl.config;

import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import net.optionfactory.miniurl.api.MinifyServlet;
import net.optionfactory.miniurl.blacklist.DefaultBlacklistService;
import net.optionfactory.miniurl.blacklist.JdbcBlacklistRepository;
import net.optionfactory.miniurl.blacklist.RegexUrlParser;
import net.optionfactory.miniurl.core.DefaultMinifyFacade;
import net.optionfactory.miniurl.dbaccess.JdbcTxManager;
import net.optionfactory.miniurl.minify.DefaultMinifyService;
import net.optionfactory.miniurl.minify.JdbcMinifiedUrlRepository;
import org.apache.derby.jdbc.EmbeddedDataSource;

@WebListener
public class WebConfigurer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final EmbeddedDataSource datasource = new EmbeddedDataSource();
        datasource.setDatabaseName("memory:minify");
        datasource.setCreateDatabase("create");
        datasource.setConnectionAttributes("");
        initDb(datasource);

        final JdbcTxManager txManager = new JdbcTxManager(datasource);
        final RegexUrlParser urlParser = new RegexUrlParser();
        urlParser.init();
        final JdbcBlacklistRepository blacklistRepo = new JdbcBlacklistRepository(txManager);
        final DefaultBlacklistService blacklistService = new DefaultBlacklistService(blacklistRepo, urlParser);
        final JdbcMinifiedUrlRepository minifiedUrlRepo = new JdbcMinifiedUrlRepository(txManager);
        final DefaultMinifyService minifyService = new DefaultMinifyService(minifiedUrlRepo);
        final DefaultMinifyFacade facade = new DefaultMinifyFacade(blacklistService, minifyService, txManager);
        final ServletRegistration.Dynamic dynReg = sce.getServletContext().addServlet(
                MinifyServlet.class.getSimpleName(), new MinifyServlet(facade));
        dynReg.setLoadOnStartup(1);
        dynReg.addMapping("/api/*");
    }

    private void initDb(DataSource ds) {
        try (final Connection conn = ds.getConnection()){
            execDDL(conn, "CREATE TABLE BLACKLIST (domain VARCHAR(255) NOT NULL, since BIGINT NOT NULL, PRIMARY KEY(domain))");
            execDDL(conn, "CREATE TABLE MINIFIED_URLS (handle VARCHAR(255) NOT NULL, target VARCHAR(255) NOT NULL, PRIMARY KEY(handle))");
            conn.commit();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    
    private static void execDDL(Connection conn, String stmt){
        try{
            conn.prepareStatement(stmt).executeUpdate();
        } catch (Exception ex) {
            //
        }    
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}