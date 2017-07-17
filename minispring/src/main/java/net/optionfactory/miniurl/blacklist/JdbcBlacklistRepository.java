package net.optionfactory.miniurl.blacklist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.optionfactory.miniurl.dbaccess.ConnectionLocator;

public class JdbcBlacklistRepository implements BlacklistRepository {

    private final ConnectionLocator<Connection> connectionLocator;

    public JdbcBlacklistRepository(ConnectionLocator<Connection> connectionLocator) {
        this.connectionLocator = connectionLocator;
    }

    @Override
    public void add(BlackListItem item) {
        try {
            final Connection conn = connectionLocator.getCurrentConnection();
            final PreparedStatement stmt = conn.prepareStatement("INSERT INTO blacklist (domain, since) VALUES (?,?)");
            stmt.setString(1, item.domain);
            stmt.setLong(2, item.since.getEpochSecond());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void remove(BlackListItem item) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<BlackListItem> find(String domain) {
        try {
            final Connection conn = connectionLocator.getCurrentConnection();
            final PreparedStatement stmt = conn.prepareStatement("SELECT domain, since FROM blacklist WHERE domain = ?");
            stmt.setString(1, domain);
            try (final ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new BlackListItem(
                        rs.getString(1),
                        Instant.ofEpochSecond(rs.getLong(2))));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<BlackListItem> findAll() {
        try {
            final Connection conn = connectionLocator.getCurrentConnection();
            final PreparedStatement stmt = conn.prepareStatement("SELECT domain, since FROM APP.blacklist");
            try (final ResultSet rs = stmt.executeQuery()) {
                final ArrayList<BlackListItem> toReturn = new ArrayList<>();
                while (rs.next()) {
                    final BlackListItem e = new BlackListItem(
                            rs.getString(1),
                            Instant.ofEpochSecond(rs.getLong(2)));
                    toReturn.add(e);
                }
                return toReturn;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
