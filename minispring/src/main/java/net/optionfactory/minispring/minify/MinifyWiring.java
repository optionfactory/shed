package net.optionfactory.minispring.minify;

import net.optionfactory.minispring.blacklist.BlacklistRepository;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinifyWiring {

    @Bean
    public MinifiedUrlRepository minifiedUrlRepository(SessionFactory hibernate) {
        return new HibernateMinifiedUrlRepository(hibernate);
    }

    @Bean
    public MinifyService minifyService(MinifiedUrlRepository minifiedUrlRepository) {
        return new HashMinifyService(minifiedUrlRepository, new MessageDigestSupplier("SHA-256"));
    }

    @Bean
    public MinifyFacade minifyFacade(MinifyService minifyService, BlacklistRepository blacklist) {
        return new MinifyFacade(minifyService, blacklist);
    }

}
