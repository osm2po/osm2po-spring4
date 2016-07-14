package de.cm.osm2po.spring4.conf;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class ServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer{

    @Override
    public void onStartup(ServletContext servletContext)
            throws ServletException {
        super.onStartup(servletContext);
//        registerServletFilter(servletContext, new AuthFilter());
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
    
}
