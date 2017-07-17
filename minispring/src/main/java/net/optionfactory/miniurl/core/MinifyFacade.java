package net.optionfactory.miniurl.core;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface MinifyFacade {

    void blacklist(String domain);

    List<BlacklistItemResponse> getBlacklistItems();

    String minify(String targetUrl) throws BlacklistedException;

    Optional<String> resolve(String handle);

    public static class BlacklistItemResponse {

        public final String domain;
        public final Instant since;

        public BlacklistItemResponse(String domain, Instant since) {
            this.domain = domain;
            this.since = since;
        }

    }
    
    public static class BlacklistedException extends RuntimeException {

        public BlacklistedException(String message) {
            super(message);
        }
    }

}
