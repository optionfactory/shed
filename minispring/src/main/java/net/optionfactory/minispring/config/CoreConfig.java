package net.optionfactory.minispring.config;

import net.optionfactory.minispring.blacklist.BlacklistRepository;
import net.optionfactory.minispring.blacklist.BlacklistWiring;
import net.optionfactory.minispring.blacklist.BlacklistedException;
import net.optionfactory.minispring.minify.MinifyService;
import net.optionfactory.minispring.minify.MinifyWiring;
import org.springframework.beans.factory.config.PreferencesPlaceholderConfigurer;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import java.net.URL;
import java.time.Clock;
import java.util.Optional;


@Configuration
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
