package net.optionfactory.minispring.minify;

import java.time.Instant;
import java.util.Optional;

public class DefaultMinifyService implements MinifyService {

    private final MinifiedUrlRepository repo;

    public DefaultMinifyService(MinifiedUrlRepository repo) {
        this.repo = repo;
    }

    @Override
    public String minify(String target) {
        // TODO: check authorization
        final String handle = generateHandle();
        repo.add(MinifiedUrl.of(handle, target));
        return handle;
    }

    @Override
    public Optional<String> resolve(String handle) {
        return repo.find(handle).map(mu -> mu.target);
    }

    private static String generateHandle() {
        // FIXME
        return String.format("%s%d", MINIFIED_URL_PREFIX, Instant.now().toEpochMilli());
    }
}
