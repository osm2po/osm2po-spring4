package de.cm.osm2po.spring4.conf;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.keycloak.adapters.servlet.KeycloakOIDCFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.FrameworkServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class ServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer{

    @Override
    public void onStartup(ServletContext servletContext)
            throws ServletException {
        super.onStartup(servletContext);
        registerServletFilter(servletContext, new ServletFilter());
        // org.keycloak.adapters.servlet.KeycloakOIDCFilter
        KeycloakOIDCFilter kcFilter = new KeycloakOIDCFilter();
        registerServletFilter(servletContext, kcFilter);
    }
    
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {WebAppContext.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
    
    @Override
    protected FrameworkServlet createDispatcherServlet(
            WebApplicationContext servletAppContext) {
        // see http://memorynotfound.com/spring-mvc-exception-handling/
        DispatcherServlet dispatcherServlet =
                (DispatcherServlet) super.createDispatcherServlet(servletAppContext);
        // Global exception not thrown by default - WTF
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
        // Alternative: Override noHandlerFound(...) of DispatcherServlet itself
        return dispatcherServlet;
    }
    
}
