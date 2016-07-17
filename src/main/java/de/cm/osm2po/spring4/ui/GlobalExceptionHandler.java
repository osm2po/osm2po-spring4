package de.cm.osm2po.spring4.ui;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    // http://memorynotfound.com/spring-mvc-exception-handling/
    
    @ExceptionHandler(Exception.class)
    @ResponseBody public String handleException(HttpServletResponse response, Exception ex) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return "Exception occured: " + ex.getMessage();
    }

}
