package net.optionfactory.minispring.minify;

import java.util.Optional;

public interface MinifiedUrlRepository {

    public void add(MinifiedUrl url);

    public Optional<MinifiedUrl> find(String handle);
}
