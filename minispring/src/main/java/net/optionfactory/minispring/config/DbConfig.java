package net.optionfactory.minispring.config;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.Properties;
import javax.sql.DataSource;
import net.optionfactory.minispring.Miniurl;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyHbmImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@EnableTransactionManagement
public class DbConfig {

    @Bean
    public DataSource dataSource() throws PropertyVetoException {
        // TODO: use a connection pool
        final EmbeddedDataSource datasource = new EmbeddedDataSource();
        datasource.setDatabaseName("memory:minify");
        datasource.setCreateDatabase("create");
        datasource.setConnectionAttributes("");
        return datasource;
    }

    @Bean
    public PlatformTransactionManager txManager(SessionFactory sessionFatory) {
        final HibernateTransactionManager bean = new HibernateTransactionManager();
        bean.setSessionFactory(sessionFatory);
        return bean;
    }

    @Bean
    public TransactionOperations transactionTemplate(PlatformTransactionManager ptm) {
        return new TransactionTemplate(ptm);
    }

    @Bean
    public LocalSessionFactoryBean localSessionFactoryBean(final DataSource dataSource) throws IOException, Exception {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.DerbyTenSevenDialect");
        hibernateProperties.put("hibernate.hbm2ddl.auto", "update");
        hibernateProperties.put("hibernate.jdbc.batch_size", "0");
        hibernateProperties.put("hibernate.cache.use_second_level_cache", "false");
        hibernateProperties.put("hibernate.cache.use_query_cache", "false");
        hibernateProperties.put("hibernate.bytecode.use_reflection_optimizer", "true");
        hibernateProperties.put("hibernate.connection.release_mode", "auto");
        hibernateProperties.put("hibernate.show_sql", "false");
        hibernateProperties.put("hibernate.format_sql", "false");
        hibernateProperties.put("hibernate.generate_statistics", "false");
        hibernateProperties.put("hibernate.default_batch_fetch_size", "200");
        final LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMappingLocations(new Resource[0]);
        factoryBean.setPackagesToScan(new String[]{Miniurl.class.getPackage().getName()});
        factoryBean.setHibernateProperties(hibernateProperties);
        factoryBean.setImplicitNamingStrategy(new ImplicitNamingStrategyLegacyHbmImpl());
        return factoryBean;
    }

}
