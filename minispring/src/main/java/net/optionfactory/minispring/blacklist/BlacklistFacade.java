package net.optionfactory.minispring.blacklist;

import java.time.Instant;
import java.util.List;

public interface BlacklistFacade {

    void blacklist(String domain, String reason);

    List<BlacklistItemResponse> getBlacklistItems();

    public void removeFromBlacklist(String domain);

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
