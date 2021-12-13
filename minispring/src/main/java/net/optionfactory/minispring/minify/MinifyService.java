package net.optionfactory.minispring.minify;

import java.net.URL;
import java.util.Optional;

public interface MinifyService {

    public static final String MINIFIED_URL_PREFIX = "mini-";

    public String minify(URL target);

    public Optional<URL> resolve(String handle);
}
