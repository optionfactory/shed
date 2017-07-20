package net.optionfactory.minispring.blacklist;

import java.util.List;

public interface BlacklistService {

    void blacklist(String domain, String reason);

    List<BlackListItem> getBlacklistItems();

    boolean isBlacklisted(String url);

    public void remove(String domain);

}
