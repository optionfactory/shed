package net.optionfactory.minispring.config;

import net.optionfactory.minispring.blacklist.BlacklistRepository;
import net.optionfactory.minispring.blacklist.BlacklistService;
import net.optionfactory.minispring.blacklist.DefaultBlacklistService;
import net.optionfactory.minispring.blacklist.HibernateBlacklistRepository;
import net.optionfactory.minispring.blacklist.JreUrlParser;
import net.optionfactory.minispring.blacklist.RegexUrlParser;
import net.optionfactory.minispring.blacklist.UrlParser;
import net.optionfactory.minispring.core.DefaultMinifyFacade;
import net.optionfactory.minispring.core.MinifyFacade;
import net.optionfactory.minispring.minify.DefaultMinifyService;
import net.optionfactory.minispring.minify.HibernateMinifiedUrlRepository;
import net.optionfactory.minispring.minify.MinifiedUrlRepository;
import net.optionfactory.minispring.minify.MinifyService;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PreferencesPlaceholderConfigurer;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;


@Configuration
@PropertySource("classpath:/net/optionfactory/miniurl/minispring.properties")
public class CoreConfig {

    @Bean
    public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        // MUST be defined in a static @Bean method, see @PropertySource javadoc
        return new PreferencesPlaceholderConfigurer();
    }
    
    @Bean
    @Primary
    public UrlParser regexUrlParser() {
        final RegexUrlParser regexUrlParser = new RegexUrlParser();
        regexUrlParser.init();
        return regexUrlParser;
    }

    @Bean
    public UrlParser jreUrlParser() {
        return new JreUrlParser();
    }
    
//    public UrlParser urlParser(
//            @Value("${url.parser.type}") String parserType
//    ) {
//        if ("jre".equals(parserType)) {
//            return new JreUrlParser();
//        }
//        RegexUrlParser regexUrlParser = new RegexUrlParser();
//        regexUrlParser.init();
//        return regexUrlParser;
//    }
    
//    public BlacklistService anotherBlacklistService(BlacklistRepository blacklistRepository) {
//        return new DefaultBlacklistService(blacklistRepository, new JreUrlParser());
//    }

    @Bean
    public BlacklistService blacklistService(BlacklistRepository blacklistRepository, UrlParser urlParser) {
        return new DefaultBlacklistService(blacklistRepository, urlParser);
    }
    
    @Bean
    public BlacklistRepository blacklistRepository(SessionFactory hibernate) {
        return new HibernateBlacklistRepository(hibernate);
    }

    @Bean
    public MinifiedUrlRepository minifiedUrlRepository(SessionFactory hibernate) {
        return new HibernateMinifiedUrlRepository(hibernate);
    }

    @Bean
    public MinifyService minifyService(MinifiedUrlRepository minifiedUrlRepository) {
        return new DefaultMinifyService(minifiedUrlRepository);
    }

    @Bean
    public MinifyFacade minifyFacade(BlacklistService blacklistService, MinifyService minifyService) {
        return new DefaultMinifyFacade(blacklistService, minifyService);
    }

}
