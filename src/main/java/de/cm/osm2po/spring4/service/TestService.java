package de.cm.osm2po.spring4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.cm.osm2po.spring4.dao.TestDao;

@Service
public class TestService {

    @Autowired TestDao testDao;
    
    @Transactional(
            transactionManager="transactionManager"/*default*/,
            rollbackFor=Throwable.class)
    public void doSomeDbTx() throws Throwable {
        testDao.doSomeDbInsert();
//        testDao.doSthWrong();
    }
    
}
