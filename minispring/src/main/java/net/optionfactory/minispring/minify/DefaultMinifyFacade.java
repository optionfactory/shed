package net.optionfactory.minispring.minify;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.optionfactory.minispring.blacklist.BlacklistService;
import net.optionfactory.minispring.blacklist.BlacklistedException;
import org.springframework.transaction.annotation.Transactional;

public class DefaultMinifyFacade implements MinifyFacade {

    private final BlacklistService blacklistService;
    private final MinifyService minifyService;

    public DefaultMinifyFacade(BlacklistService blacklistService, MinifyService minifyService) {
        this.blacklistService = blacklistService;
        this.minifyService = minifyService;
    }

    @Override
    @Transactional()
    public String minify(String target) {
        if (blacklistService.isBlacklisted(target)) {
            throw new BlacklistedException("Target url cannot be minified, it is blacklisted");
        }
        return minifyService.minify(target);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<String> resolve(String handle) {
        return minifyService.resolve(handle);
    }
}
