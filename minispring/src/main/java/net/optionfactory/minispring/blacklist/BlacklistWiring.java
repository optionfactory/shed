package net.optionfactory.minispring.blacklist;

import net.optionfactory.minispring.minify.UrlValidator;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URL;
import java.time.Clock;

@Configuration
public class BlacklistWiring {

    @Bean
    public BlacklistRepository blacklistRepository(SessionFactory hibernate) {
        return new HibernateBlacklistRepository(hibernate);
    }

    @Bean
    public BlacklistFacade blacklistFacade(Clock clock, BlacklistRepository blacklist) {
        return new BlacklistFacade(clock, blacklist);
    }

    @Bean
    public NotBlacklisted blacklistingAspect(BlacklistRepository blacklist) {
        return new NotBlacklisted(blacklist);
    }

    public static class NotBlacklisted implements UrlValidator {
        private final BlacklistRepository blacklist;

        public NotBlacklisted(BlacklistRepository blacklist) {
            this.blacklist = blacklist;
        }

        public boolean isValid(URL url) {
            return !blacklist.containsItemFor(url);
        }
    }
}
