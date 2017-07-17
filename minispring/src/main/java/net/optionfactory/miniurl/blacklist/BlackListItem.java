package net.optionfactory.miniurl.blacklist;

import java.time.Instant;

public class BlackListItem {

    public final String domain;
    public final Instant since;

    public BlackListItem(String domain, Instant since) {
        this.domain = domain;
        this.since = since;
    }

}
