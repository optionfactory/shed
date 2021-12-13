package net.optionfactory.minispring.config;

import net.optionfactory.minispring.blacklist.BlacklistRepository;
import net.optionfactory.minispring.blacklist.BlacklistWiring;
import net.optionfactory.minispring.blacklist.BlacklistedException;
import net.optionfactory.minispring.minify.MinifyWiring;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.config.PreferencesPlaceholderConfigurer;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import java.net.URL;
import java.time.Clock;


@Configuration
@EnableAspectJAutoProxy
@PropertySource("classpath:/net/optionfactory/miniurl/minispring.properties")
@Import({BlacklistWiring.class,
        MinifyWiring.class})
public class CoreConfig {

    @Bean
    public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        // MUST be defined in a static @Bean method, see @PropertySource javadoc
        return new PreferencesPlaceholderConfigurer();
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public BlacklistingAspect blacklistingAspect(BlacklistRepository blacklist) {
        return new BlacklistingAspect(blacklist);
    }

    @Aspect
    public static class BlacklistingAspect {
        private final BlacklistRepository blacklist;

        public BlacklistingAspect(BlacklistRepository blacklist) {
            this.blacklist = blacklist;
        }

        @Before("execution(* net.optionfactory.minispring.minify.MinifyService.minify(java.net.URL))")
        public void throwIfBlacklisted(JoinPoint joinPoint) {
            final URL target = (URL) joinPoint.getArgs()[0];
            if (blacklist.containsItemFor(target)) {
                throw new BlacklistedException("Domain is blacklisted");
            }
        }

    }
}
