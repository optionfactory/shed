package net.optionfactory.minispring.minify;

import org.apache.commons.codec.binary.Hex;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class HashMinifyService implements MinifyService {

    private final MinifiedUrlRepository repo;
    private final Supplier<MessageDigest> messageDigestSupplier;
    private final List<UrlValidator> urlValidators;

    public HashMinifyService(MinifiedUrlRepository repo, Supplier<MessageDigest> messageDigestSupplier, List<UrlValidator> urlValidators) {
        this.repo = repo;
        this.messageDigestSupplier = messageDigestSupplier;
        this.urlValidators = urlValidators;
    }

    @Override
    public String minify(URL target) {
        if (urlValidators.stream().anyMatch(e -> e.isValid(target))) {
            throw new UrlNotValidException("Url is not allowed");
        }
        // TODO: check authorization
        final String handle = generateHandle(target);
        repo.add(MinifiedUrl.of(handle, target));
        return handle;
    }

    @Override
    public Optional<URL> resolve(String handle) {
        return repo.find(handle).map(mu -> mu.target);
    }

    private String generateHandle(URL target) {
        try {
            final byte[] digest = messageDigestSupplier.get().digest(target.toURI().toString().getBytes(StandardCharsets.UTF_8));
            return String.format("%s%s", MINIFIED_URL_PREFIX, Hex.encodeHexString(digest));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }
}
