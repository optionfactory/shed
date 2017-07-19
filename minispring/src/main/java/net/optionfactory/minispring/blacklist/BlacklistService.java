package net.optionfactory.minispring.blacklist;

import java.util.List;

public interface BlacklistService {

    void blacklist(String domain);

    List<BlackListItem> getBlacklistItems();

    boolean isBlacklisted(String url);

}
