package net.optionfactory.miniurl.dbaccess;


public interface ConnectionLocator<T> {
    T getCurrentConnection();
}
