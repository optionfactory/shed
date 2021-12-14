package net.optionfactory.minispring.blacklist;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.optionfactory.minispring.blacklist.api.v1.BlacklistController;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
public class BlacklistFacade {
    private final Clock clock;
    private final BlacklistRepository blacklist;
    private BlacklistMapper blacklistMapper;

    public BlacklistFacade(Clock clock, BlacklistRepository blacklist, BlacklistMapper blacklistMapper) {
        this.clock = clock;
        this.blacklist = blacklist;
        this.blacklistMapper = blacklistMapper;
    }

    public void blacklist(String domain, String reason) {
        blacklist.add(new BlackListItem(domain, reason, clock.instant()));
    }

    public void blacklist(BlacklistController.BlacklistRequest request) {
        final BlackListItem blackListItem = blacklistMapper.fromDto(request);
        blackListItem.since = clock.instant();
        blacklist.add(blackListItem);
    }

    public void removeFromBlacklist(String domain) {
        blacklist.find(domain).ifPresent(blacklist::remove);
    }

    @Transactional(readOnly = true)
    public List<BlacklistItemResponse> getBlacklistItems() {
        return blacklist.findAll()
                .stream()
                .map(blacklistMapper::fromDto)
                .collect(Collectors.toList());
    }

    public static class BlacklistItemResponse {
        public final String domain;
        public final String reason;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssxxx", timezone = "UTC")
        public final Instant since;

        public BlacklistItemResponse(String domain, String reason, Instant since) {
            this.domain = domain;
            this.reason = reason;
            this.since = since;
        }
    }

}
