package de.cm.osm2po.spring4.conf;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.Base64Utils;
import org.springframework.web.filter.OncePerRequestFilter;

public class AuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String xAuth = request.getHeader("Authorization");
        
        if (null == xAuth) {
            // http://www.coderanch.com/t/352345/Servlets/java/HTTP-basic-authentication-Web-Applications
            response.setHeader("WWW-Authenticate", "BASIC realm=\"REALM2PO\"");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } else {
            String up = xAuth.split(" ")[1].trim();
            System.out.println(new String(Base64Utils.decodeFromString(up)));
        }
   
        System.out.println(request.getRemoteHost());
        
        // Create our Authentication and let Spring know about it
        //        Authentication auth = new UsernamePasswordAuthenticationToken("user", "pass");
        //        SecurityContextHolder.getContext().setAuthentication(auth);

        response.addHeader("AuthToken", "4711"); // Must be called before response is created
        filterChain.doFilter(request, response);
    }

}
