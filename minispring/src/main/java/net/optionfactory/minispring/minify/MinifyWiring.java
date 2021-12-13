package net.optionfactory.minispring.minify;

import net.optionfactory.minispring.blacklist.BlacklistService;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Supplier;

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
    public MinifyFacade minifyFacade(BlacklistService blacklistService, MinifyService minifyService) {
        return new DefaultMinifyFacade(blacklistService, minifyService);
    }

    public static class MessageDigestSupplier implements Supplier<MessageDigest> {
        private final String algo;

        public MessageDigestSupplier(String algo) {
            this.algo = algo;
        }

        @Override
        public MessageDigest get() {
            try {
                return MessageDigest.getInstance(algo);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
