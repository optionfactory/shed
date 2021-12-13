package net.optionfactory.minispring.minify;

import net.optionfactory.minispring.blacklist.BlacklistRepository;
import net.optionfactory.minispring.blacklist.BlacklistedException;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.Optional;

public class MinifyFacade {

    private final MinifyService minifier;
    private final BlacklistRepository blacklist;

    public MinifyFacade(MinifyService minifier, BlacklistRepository blacklist) {
        this.minifier = minifier;
        this.blacklist = blacklist;
    }

    @Transactional
    public String minify(URL target) {
        if (blacklist.containsItemFor(target)) {
            throw new BlacklistedException("Domain is blacklisted");
        }
        return minifier.minify(target);
    }

    @Transactional(readOnly = true)
    public Optional<URL> resolve(String handle) {
        return minifier.resolve(handle);
    }
}
