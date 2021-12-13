package net.optionfactory.minispring.blacklist;

import java.net.URL;
import java.util.List;
import java.util.Optional;

public interface BlacklistRepository {

    void add(BlackListItem item);

    Optional<BlackListItem> find(String domain);

    boolean containsItemFor(URL url);

    List<BlackListItem> findAll();

    void remove(BlackListItem item);

}
