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

}
