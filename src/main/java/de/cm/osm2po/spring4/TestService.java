package de.cm.osm2po.spring4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestService {

    @Autowired TestDao testDao;
    
    @Transactional(rollbackFor=Throwable.class)
    public void doSomeDbTx() throws Throwable {
        testDao.doSomeDbInsert();
//        testDao.doSthWrong();
    }
    
}
