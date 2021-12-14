package net.optionfactory.minispring.minify;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URL;
import java.util.List;

@Configuration
public class MinifyWiring {

    @Bean
    public MinifiedUrlRepository minifiedUrlRepository(SessionFactory hibernate) {
        return new HibernateMinifiedUrlRepository(hibernate);
    }

    @Bean
    public MinifyService minifyService(MinifiedUrlRepository minifiedUrlRepository, List<UrlValidator> urlValidators) {
        return new HashMinifyService(minifiedUrlRepository, new MessageDigestSupplier("SHA-256"), urlValidators);
    }

    @Bean
    public UrlValidator notLocalhost() {
        return new NotLocalhostUrlValidator();
    }

    @Bean
    public MinifyFacade minifyFacade(MinifyService minifyService) {
        return new MinifyFacade(minifyService);
    }

    private static class NotLocalhostUrlValidator implements UrlValidator {
        @Override
        public boolean isValid(URL url) {
            return !url.getHost().equals("localhost");
        }
    }
}
