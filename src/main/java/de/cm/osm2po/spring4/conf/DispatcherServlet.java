package de.cm.osm2po.spring4.conf;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class DispatcherServlet extends AbstractAnnotationConfigDispatcherServletInitializer{

    @Override
    public void onStartup(ServletContext servletContext)
            throws ServletException {
        registerServletFilter(servletContext, new AuthFilter());
        super.onStartup(servletContext);
    }
    
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {RootContext.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return null;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
    
}
