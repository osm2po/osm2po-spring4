package de.cm.osm2po.spring4;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.cm.osm2po.Config;
import de.cm.osm2po.misc.Utils;
import de.cm.osm2po.misc.WkbUtils;
import de.cm.osm2po.model.LatLon;
import de.cm.osm2po.service.GeoJson;

@Controller
@SessionAttributes("foo")
public class TestController {
    private Config config;
    
    @Autowired private DataSource dataSource;
    
    @Autowired private MessageSource messageSource; 

    @Autowired private TestComponent1 testComponent1;

    private TestComponent2 testComponent2;
    @Autowired public TestController(TestComponent2 testComponent2) {
        this.testComponent2 = testComponent2;
    }
    
    @PostConstruct
    void init() {
        (this.config = new Config()).getLog().unlock().info("osm2po configured");;
        
    }
    
    @PreDestroy
    void clean() {
        this.config.close();
    }
    
    @RequestMapping(value="/hello/{name}", method=RequestMethod.GET)
    String hello(@PathVariable String name) {
        return "Hello " + name;
    }

    @GetMapping(value="/account", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody Account account(ModelMap model) {
        return new Account();
    }
    
    // Official but unknown MIME TYPE to most Browsers: application/vnd.geo+json
    // Typical reaction: Download Dialog
    // see: https://sgillies.net//2014/05/22/the-geojson-media-type.html
    @RequestMapping(value="/geojson", produces="application/json;charset=UTF-8")
    @ResponseBody String geojson(Locale locale) {
        String message = messageSource.getMessage("welcome", null, locale);
        System.err.println(message);
        
        String geojson =  new GeoJson().featureCollection()
                .addPoint(new LatLon(53.5, 10.1)).with("foo", "bar")
                .flush();
        return geojson;
    }
    
    @PostMapping("/foo")
    String foo(@ModelAttribute("account") Account account, BindingResult bindingResult) {
        if ("Kuno".equals(account.getUser())) {
            FieldError fe = new FieldError("user", "user", "Doof");
            bindingResult.addError(fe);
        }
        return "foo";
    }

    
    // Muss laut Doku public sein
    @Transactional
    public String someDbStuff() {
        try {
            // DataSourceUtils instead of dataSource.getConnection()
            Connection con = DataSourceUtils.getConnection(dataSource);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM hh_2po_4pgr WHERE id=1");
            rs.next();
            String way = rs. getString("geom_way");
            Object wkbObj = WkbUtils.fromWkb(Utils.hexToBytes(way));
            stm.close();
            return wkbObj.toString();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @GetMapping("/foo")
    String foo(ModelMap model) {
        System.out.println(someDbStuff());
        
        if (null == model.get("foo")) { // @SessionAttributes("foo") s.o.
            model.addAttribute("foo", this.toString()
                    + " " + testComponent1.get4711()
                    + " " + testComponent2.get4712());
        }
        model.addAttribute("account", new Account());
        
        return "foo";
    }

    @RequestMapping(value="/jsonio")
    @ResponseBody Account jsonio() {
        ObjectMapper om = new ObjectMapper();
        
        String json = "{\"user\":\"Kuno\",\"pass\":\"Oyten\"}";
        Account account = null;
        
        try {
            account = om.readValue(json, Account.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return account;
    }

    
}
