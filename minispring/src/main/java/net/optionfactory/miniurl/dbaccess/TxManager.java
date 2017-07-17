package net.optionfactory.miniurl.dbaccess;

import java.util.function.Supplier;

public interface TxManager {

    <R> R executeInTransaction(Supplier<R> f);

    void executeInTransaction(Runnable f);
}
