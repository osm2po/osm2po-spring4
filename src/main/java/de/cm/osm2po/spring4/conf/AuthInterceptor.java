package de.cm.osm2po.spring4.conf;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import de.cm.osm2po.spring4.service.DbService;

public class AuthInterceptor extends HandlerInterceptorAdapter {

    private final static Log log = LogFactory.getLog(AuthInterceptor.class);
    
    @Autowired
    DbService dbService;
    
    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            Method method = ((HandlerMethod) handler).getMethod();
            log.info(method.getName());
            
            String xAuth = request.getHeader("Authorization");
            
            if (null == xAuth) {
                // http://www.coderanch.com/t/352345/Servlets/java/HTTP-basic-authentication-Web-Applications
                response.setHeader("WWW-Authenticate", "BASIC realm=\"REALM2PO\"");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            } else {
                String up = xAuth.split(" ")[1].trim();
                System.out.println(new String(Base64Utils.decodeFromString(up)));
            }
       
            System.out.println(request.getRemoteHost());
            
            // Create our Authentication and let Spring know about it
            //        Authentication auth = new UsernamePasswordAuthenticationToken("user", "pass");
            //        SecurityContextHolder.getContext().setAuthentication(auth);

            response.addHeader("AuthToken", "4711"); // Must be called before response is created
        }
        return true;
    }
}
