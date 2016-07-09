package de.cm.osm2po.spring4.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public class TestDao {

    @Autowired private DataSource dataSource;
    
    public void doSomeDbInsert() throws SQLException{
        // WICHTIG - sonst kein @Transactional!
        // DatSourceUtils.getConnection(dataSource) statt dataSource.getConnection()
        Connection con = DataSourceUtils.getConnection(dataSource);
        Statement stm = con.createStatement();
        int id = new Random().nextInt();
        stm.executeUpdate("INSERT INTO persons VALUES (" + id + ", 'Kuno')");
        stm.close();
    }
    
    public void doSthWrong() {
        throw new RuntimeException("urps");
    }
    
    

}
