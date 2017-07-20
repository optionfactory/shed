package net.optionfactory.minispring.blacklist;

import java.time.Instant;
import java.util.List;

public class DefaultBlacklistService implements BlacklistService {

    private final BlacklistRepository blacklist;
    private final UrlParser urlParser;

    public DefaultBlacklistService(BlacklistRepository blacklist, UrlParser urlParser) {
        this.blacklist = blacklist;
        this.urlParser = urlParser;
    }

    @Override
    public void blacklist(String domain, String reason) {
        // TODO: authorization check
        blacklist.add(new BlackListItem(domain, reason, Instant.now()));
    }

    @Override
    public void remove(String domain) {
        blacklist.find(domain).ifPresent(blacklist::remove);
    }

    @Override
    public boolean isBlacklisted(String url) {
        final String domain = urlParser.getHost(url);
        return blacklist.find(domain).isPresent();
    }

    @Override
    public List<BlackListItem> getBlacklistItems() {
        return blacklist.findAll();
    }
    
    

}
