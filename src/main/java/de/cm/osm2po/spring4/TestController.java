package de.cm.osm2po.spring4;

import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
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
import de.cm.osm2po.model.LatLon;
import de.cm.osm2po.service.GeoJson;

@Controller
@SessionAttributes("foo")
public class TestController {
    private Config config;
    
    @Autowired private MessageSource messageSource; 
    @Autowired private TestComponent1 testComponent1;
    @Autowired private TestService testService;
    
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
    
    @GetMapping("/foo")
    String foo(ModelMap model) {
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
            testService.doSomeDbTx();
            account = om.readValue(json, Account.class);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        
        return account;
    }

    
}
