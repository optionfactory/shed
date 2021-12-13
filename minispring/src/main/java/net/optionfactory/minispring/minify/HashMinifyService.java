package net.optionfactory.minispring.minify;

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Optional;
import java.util.function.Supplier;

public class HashMinifyService implements MinifyService {

    private final MinifiedUrlRepository repo;
    private final Supplier<MessageDigest> messageDigestSupplier;

    public HashMinifyService(MinifiedUrlRepository repo, Supplier<MessageDigest> messageDigestSupplier) {
        this.repo = repo;
        this.messageDigestSupplier = messageDigestSupplier;
    }

    @Override
    public String minify(String target) {
        // TODO: check authorization
        final String handle = generateHandle(target);
        repo.add(MinifiedUrl.of(handle, target));
        return handle;
    }

    @Override
    public Optional<String> resolve(String handle) {
        return repo.find(handle).map(mu -> mu.target);
    }

    private String generateHandle(String target) {
        final byte[] digest = messageDigestSupplier.get().digest(target.getBytes(StandardCharsets.UTF_8));
        return String.format("%s%s", MINIFIED_URL_PREFIX, Hex.encodeHexString(digest));
    }
}
