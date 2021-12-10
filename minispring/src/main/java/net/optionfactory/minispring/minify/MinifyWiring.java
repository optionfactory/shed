package net.optionfactory.minispring.minify;

import net.optionfactory.minispring.blacklist.BlacklistService;
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
        return new DefaultMinifyService(minifiedUrlRepository);
    }

    @Bean
    public MinifyFacade minifyFacade(BlacklistService blacklistService, MinifyService minifyService) {
        return new DefaultMinifyFacade(blacklistService, minifyService);
    }
}
