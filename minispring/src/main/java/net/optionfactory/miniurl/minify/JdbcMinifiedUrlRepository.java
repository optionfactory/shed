package net.optionfactory.miniurl.minify;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import net.optionfactory.miniurl.dbaccess.ConnectionLocator;

public class JdbcMinifiedUrlRepository implements MinifiedUrlRepository {

    private final ConnectionLocator<Connection> connectionLocator;

    public JdbcMinifiedUrlRepository(ConnectionLocator<Connection> connectionLocator) {
        this.connectionLocator = connectionLocator;
    }

    @Override
    public void add(MinifiedUrl url) {
        try {
            final Connection conn = connectionLocator.getCurrentConnection();
            final PreparedStatement stmt = conn.prepareStatement("INSERT INTO minified_urls (handle, target) VALUES (?,?)");
            stmt.setString(1, url.handle);
            stmt.setString(2, url.target);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Optional<MinifiedUrl> find(String handle) {
        try {
            final Connection conn = connectionLocator.getCurrentConnection();
            final PreparedStatement stmt = conn.prepareStatement("SELECT handle, target FROM minified_urls WHERE handle = ?");
            stmt.setString(1, handle);
            try (final ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new MinifiedUrl(
                        rs.getString(1),
                        rs.getString(2)));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
