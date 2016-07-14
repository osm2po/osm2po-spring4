package de.cm.osm2po.spring4.service;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import de.cm.osm2po.logging.LogJclWriter;
import de.cm.osm2po.routing.Graph;

@Service
public class Osm2poService {
    
    private static final Log log = LogFactory.getLog(Osm2poService.class);

    @Autowired Environment env;
    
    private Graph graph;
    
    @PostConstruct
    public void init() {
        String graphFilePath = env.getProperty("osm2po.graphfile");
        log.info(graphFilePath);

        File graphFile = new File(graphFilePath);
        
        de.cm.osm2po.logging.Log log2 =
                new de.cm.osm2po.logging.Log().addLogWriter(new LogJclWriter());
        graph = new Graph(graphFile, log2, Graph.SUPPORT_LATLON, true, null);
    }
    
    @PreDestroy
    public void close() {
        graph.close();
    }
    
}
