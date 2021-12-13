package net.optionfactory.minispring.minify;

import net.optionfactory.minispring.blacklist.BlacklistedException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface MinifyFacade {



    String minify(String targetUrl) throws BlacklistedException;

    Optional<String> resolve(String handle);


}