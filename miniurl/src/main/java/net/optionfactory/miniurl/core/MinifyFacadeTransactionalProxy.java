/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.optionfactory.miniurl.core;

import java.util.List;
import java.util.Optional;
import net.optionfactory.miniurl.dbaccess.TxManager;

/**
 *
 * @author fdegrassi
 */
public class MinifyFacadeTransactionalProxy implements MinifyFacade {
    private final MinifyFacade inner;
    private final TxManager txManager;

    public MinifyFacadeTransactionalProxy(MinifyFacade inner, TxManager txManager) {
        this.inner = inner;
        this.txManager = txManager;
    }

    @Override
    public void blacklist(String domain) {
        txManager.executeInTransaction(() -> inner.blacklist(domain));
    }

    @Override
    public List<BlacklistItemResponse> getBlacklistItems() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String minify(String targetUrl) throws BlacklistedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Optional<String> resolve(String handle) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
