package net.optionfactory.minispring.config;

import net.optionfactory.minispring.BaseMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.optionfactory.minispring.BaseMapper;
import net.optionfactory.minispring.blacklist.BlacklistWiring;
import net.optionfactory.minispring.minify.MinifyWiring;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.config.PreferencesPlaceholderConfigurer;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

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
    public BaseMapper baseMapper() {
        return Mappers.getMapper(BaseMapper.class);
    }
    @Bean
    public CsvMapper csvMapper() {
        final CsvMapper csvMapper = new CsvMapper();
        csvMapper.registerModules(
                new JavaTimeModule(),
                new Jdk8Module());
        csvMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return csvMapper;

    }
}
