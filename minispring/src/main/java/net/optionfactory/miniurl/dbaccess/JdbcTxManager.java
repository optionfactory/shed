package net.optionfactory.miniurl.dbaccess;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;
import javax.sql.DataSource;

public class JdbcTxManager implements TxManager, ConnectionLocator<Connection> {

    private final DataSource dataSource;
    private final ThreadLocal<Connection> threadLocalConnection = new ThreadLocal<>();

    public JdbcTxManager(DataSource ds) {
        this.dataSource = ds;
    }

    @Override
    public <R> R executeInTransaction(Supplier<R> f) {
        return executeInTransactionInternal(f);
    }

    @Override
    public void executeInTransaction(Runnable f) {
        executeInTransactionInternal(() -> {
            f.run();
            return null;
        });

    }

    @Override
    public Connection getCurrentConnection() {
        return threadLocalConnection.get();
    }

    private <R> R executeInTransactionInternal(Supplier<R> f) throws RuntimeException {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            threadLocalConnection.set(conn);
            try {
                final R result = f.get();
                conn.commit();
                return result;
            } catch (Exception ex) {
                if (!conn.isClosed()) {
                    conn.rollback();
                }
                throw ex;
            } finally {
                threadLocalConnection.remove();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
