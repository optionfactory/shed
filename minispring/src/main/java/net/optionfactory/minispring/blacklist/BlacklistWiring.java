package net.optionfactory.minispring.blacklist;

import net.optionfactory.minispring.minify.DefaultMinifyFacade;
import net.optionfactory.minispring.minify.MinifyFacade;
import net.optionfactory.minispring.minify.MinifyService;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class BlacklistWiring {

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
    public BlacklistFacade blacklistFacade(BlacklistService blacklistService) {
        return new BlacklistFacadeTransactionalImpl(blacklistService);
    }
}
