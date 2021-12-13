package net.optionfactory.minispring.minify;

import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.Optional;

public class MinifyFacade {

    private final MinifyService minifier;

    public MinifyFacade(MinifyService minifier) {
        this.minifier = minifier;
    }

    @Transactional
    public String minify(URL target) {

        return minifier.minify(target);
    }

    @Transactional(readOnly = true)
    public Optional<URL> resolve(String handle) {
        return minifier.resolve(handle);
    }
}
