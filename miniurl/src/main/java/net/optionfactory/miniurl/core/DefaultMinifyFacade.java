package net.optionfactory.miniurl.core;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.optionfactory.miniurl.blacklist.BlacklistService;
import net.optionfactory.miniurl.dbaccess.TxManager;
import net.optionfactory.miniurl.minify.MinifyService;

public class DefaultMinifyFacade implements MinifyFacade {

    private final BlacklistService blacklistService;
    private final MinifyService minifyService;
    private final TxManager txManager;

    public DefaultMinifyFacade(BlacklistService blacklistService, MinifyService minifyService, TxManager txManager) {
        this.blacklistService = blacklistService;
        this.minifyService = minifyService;
        this.txManager = txManager;
    }

    @Override
    public void blacklist(String domain) {
        txManager.executeInTransaction(() -> {
            blacklistService.blacklist(domain);
        });
    }

    @Override
    public List<BlacklistItemResponse> getBlacklistItems() {
        return txManager.executeInTransaction(() -> {
            return blacklistService.getBlacklistItems()
                    .stream()
                    .map(item -> new BlacklistItemResponse(item.domain, item.since))
                    .collect(Collectors.toList());
        });
    }

    @Override
    public String minify(String target) {
        return txManager.executeInTransaction(() -> {
            if (blacklistService.isBlacklisted(target)) {
                throw new BlacklistedException("Target url cannot be minified, it is blacklisted");
            }
            return minifyService.minify(target);
        });
    }

    @Override
    public Optional<String> resolve(String handle) {
        return txManager.executeInTransaction(() -> {
            return minifyService.resolve(handle);
        });
    }

}
