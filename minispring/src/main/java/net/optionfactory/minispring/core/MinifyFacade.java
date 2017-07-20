package net.optionfactory.minispring.core;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface MinifyFacade {

    void blacklist(String domain, String reason);

    List<BlacklistItemResponse> getBlacklistItems();

    String minify(String targetUrl) throws BlacklistedException;

    Optional<String> resolve(String handle);

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

    public static class BlacklistedException extends RuntimeException {

        public BlacklistedException(String message) {
            super(message);
        }
    }

}
