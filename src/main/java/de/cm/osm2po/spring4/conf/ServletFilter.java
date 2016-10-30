package de.cm.osm2po.spring4.conf;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.OncePerRequestFilter;

public class ServletFilter extends OncePerRequestFilter {
    private final static Log log = LogFactory.getLog(ServletFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    	log.info("Filtering request from " + request.getRemoteHost());
        filterChain.doFilter(request, response);
    }

}
