package de.cm.osm2po.spring4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.cm.osm2po.spring4.dao.DbDao;

@Service
public class DbService {

    @Autowired DbDao dao;
    
    //Defaults to: transactionManager="transactionManager"
    @Transactional(rollbackFor=Throwable.class)
    public void doSomeDbTx() throws Throwable {
        dao.doSomeDbInsert();
    }
    
}
