package net.optionfactory.miniurl.blacklist;

import java.util.List;
import java.util.Optional;

public interface BlacklistRepository {

    void add(BlackListItem item);

    Optional<BlackListItem> find(String domain);

    List<BlackListItem> findAll();

    void remove(BlackListItem item);

}
