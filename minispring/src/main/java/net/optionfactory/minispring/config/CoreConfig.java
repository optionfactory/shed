package net.optionfactory.minispring.config;

import net.optionfactory.minispring.blacklist.BlacklistWiring;
import net.optionfactory.minispring.minify.MinifyWiring;
import org.springframework.beans.factory.config.PreferencesPlaceholderConfigurer;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
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
