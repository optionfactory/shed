package net.optionfactory.minispring.blacklist;

import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
public class BlacklistFacade {
    private final Clock clock;
    private final BlacklistRepository blacklist;

    public BlacklistFacade(Clock clock, BlacklistRepository blacklist) {
        this.clock = clock;
        this.blacklist = blacklist;
    }

    public void blacklist(String domain, String reason) {
        blacklist.add(new BlackListItem(domain, reason, clock.instant()));
    }

    public void removeFromBlacklist(String domain) {
        blacklist.find(domain).ifPresent(blacklist::remove);
    }

    @Transactional(readOnly = true)
    public List<BlacklistItemResponse> getBlacklistItems() {
        return blacklist.findAll()
                .stream()
                .map(item -> new BlacklistItemResponse(item.domain, item.reason, item.since))
                .collect(Collectors.toList());
    }

    public static class BlacklistItemResponse {
        public final String domain;
        public final String reason;
        public final Instant since;

        public BlacklistItemResponse(String domain, String reason, Instant since) {
            this.domain = domain;
            this.reason = reason;
            this.since = since;
        }
    }

}
