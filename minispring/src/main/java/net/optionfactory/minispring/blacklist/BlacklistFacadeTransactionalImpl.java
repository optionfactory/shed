package net.optionfactory.minispring.blacklist;

import net.optionfactory.minispring.minify.MinifyService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
public class BlacklistFacadeTransactionalImpl implements BlacklistFacade {

    private final BlacklistService blacklistService;

    public BlacklistFacadeTransactionalImpl(BlacklistService blacklistService) {
        this.blacklistService = blacklistService;
    }

    @Override
    public void blacklist(String domain, String reason) {
        blacklistService.blacklist(domain, reason);
    }

    @Override
    public void removeFromBlacklist(String domain) {
        blacklistService.remove(domain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlacklistItemResponse> getBlacklistItems() {
        return blacklistService.getBlacklistItems()
                .stream()
                .map(item -> new BlacklistItemResponse(item.domain, item.reason, item.since))
                .collect(Collectors.toList());
    }

}
