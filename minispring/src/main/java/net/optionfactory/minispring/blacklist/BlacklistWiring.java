package net.optionfactory.minispring.blacklist;

import net.optionfactory.minispring.minify.MinifyService;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.net.URL;
import java.time.Clock;
import java.util.Optional;

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
    @Primary
    public MinifyService minifier(MinifyService minifyService, BlacklistRepository blacklist) {
        return new MinifyService() {
            @Override
            public String minify(URL target) {
                if (blacklist.containsItemFor(target)) {
                    throw new BlacklistedException("Domain is blacklisted");
                }
                return minifyService.minify(target);
            }

            @Override
            public Optional<URL> resolve(String handle) {
                return minifyService.resolve(handle);
            }
        };
    }
}
