package net.optionfactory.minispring.minify;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Supplier;

public class MessageDigestSupplier implements Supplier<MessageDigest> {
    private final String algo;

    public MessageDigestSupplier(String algo) {
        this.algo = algo;
    }

    @Override
    public MessageDigest get() {
        try {
            return MessageDigest.getInstance(algo);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
