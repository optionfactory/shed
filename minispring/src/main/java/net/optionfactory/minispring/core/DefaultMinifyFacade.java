package net.optionfactory.minispring.core;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.optionfactory.minispring.blacklist.BlacklistService;
import net.optionfactory.minispring.minify.MinifyService;
import org.springframework.transaction.annotation.Transactional;

public class DefaultMinifyFacade implements MinifyFacade {

    private final BlacklistService blacklistService;
    private final MinifyService minifyService;

    public DefaultMinifyFacade(BlacklistService blacklistService, MinifyService minifyService) {
        this.blacklistService = blacklistService;
        this.minifyService = minifyService;
    }

    @Override
    @Transactional
    public void blacklist(String domain) {
        blacklistService.blacklist(domain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlacklistItemResponse> getBlacklistItems() {
        return blacklistService.getBlacklistItems()
                .stream()
                .map(item -> new BlacklistItemResponse(item.domain, item.since))
                .collect(Collectors.toList());
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
