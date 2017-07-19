package net.optionfactory.minispring.minify;

import java.util.Optional;

public interface MinifyService {

    public static final String MINIFIED_URL_PREFIX = "mini-";

    public String minify(String target);

    public Optional<String> resolve(String handle);
}
