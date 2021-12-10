package net.optionfactory.minispring.config;

import net.optionfactory.minispring.blacklist.BlacklistRepository;
import net.optionfactory.minispring.blacklist.BlacklistService;
import net.optionfactory.minispring.blacklist.BlacklistWiring;
import net.optionfactory.minispring.blacklist.DefaultBlacklistService;
import net.optionfactory.minispring.blacklist.HibernateBlacklistRepository;
import net.optionfactory.minispring.blacklist.JreUrlParser;
import net.optionfactory.minispring.blacklist.RegexUrlParser;
import net.optionfactory.minispring.blacklist.UrlParser;
import net.optionfactory.minispring.minify.DefaultMinifyFacade;
import net.optionfactory.minispring.minify.MinifyFacade;
import net.optionfactory.minispring.minify.DefaultMinifyService;
import net.optionfactory.minispring.minify.HibernateMinifiedUrlRepository;
import net.optionfactory.minispring.minify.MinifiedUrlRepository;
import net.optionfactory.minispring.minify.MinifyService;
import net.optionfactory.minispring.minify.MinifyWiring;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.config.PreferencesPlaceholderConfigurer;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;


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


}
