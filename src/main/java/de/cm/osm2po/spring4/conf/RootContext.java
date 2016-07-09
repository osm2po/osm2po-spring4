package de.cm.osm2po.spring4.conf;

import java.util.Locale;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

// <mvc:annotation-driven />
@Configuration

// <context:component-scan base-package="com.mkyong.helloworld.web" />
@EnableWebMvc

// <context:component-scan base-package="de.cm.osm2po.spring4" />
@ComponentScan(basePackages = { // TODO Better: basePackageClasses with foo-marker
        "de.cm.osm2po.spring4.ui",
        "de.cm.osm2po.spring4.dao",
        "de.cm.osm2po.spring4.service"
        })

// Will be autowired into Environment env. See below.
@PropertySource(ignoreResourceNotFound=true, value={
        "${catalina.base/conf/db.properties}",
        "classpath:db.properties"})

// <tx:annotation-driven transaction-manager="txManager" proxy-target-class="false" />
@EnableTransactionManagement(proxyTargetClass=false) // false is default

public class RootContext extends WebMvcConfigurerAdapter {

    @Autowired
    private Environment env;
    
    
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serves static content like images, css, etc.
        // <mvc:resources mapping="/res/**" location="/res/" />
        registry.addResourceHandler("/res/**").addResourceLocations("/res/");
    }

    /*------------------------------------------------------------------------------------
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="viewClass" value="org.springframework.web.servlet.view.JstlView"/>
        <property name="prefix" value="/WEB-INF/views/jsp/" />
        <property name="suffix" value=".jsp" />
    </bean>
    -------------------------------------------------------------------------------------*/
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;

    }

    /*---------------------------------------------------------------------------------------------------------
    <bean id="messageSource" class="org.springframework.context.support.ReloadableResourceBundleMessageSource">
        <property name="basename" value="classpath:messages" />
        <property name="defaultEncoding" value="UTF-8" />
    </bean>
    ----------------------------------------------------------------------------------------------------------*/
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
    
    /*--------------------------------------------------------------------------------------------
    <bean id="localeResolver"  class="org.springframework.web.servlet.i18n.SessionLocaleResolver">
        <property name="defaultLocale" value="en" />
    </bean>
    ---------------------------------------------------------------------------------------------*/
    @Bean
    public LocaleResolver localeResolver(){
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(new Locale("en"));
        return resolver;
    }
    
    /*--------------------------------------------------------------------------------
    <mvc:interceptors>
        <mvc:interceptor>
            <mvc:mapping path="/**" />
            <bean class="org.springframework.web.servlet.i18n.LocaleChangeInterceptor">
                <property name="paramName" value="lang" />
            </bean>
        </mvc:interceptor>
    </mvc:interceptors>
    ---------------------------------------------------------------------------------*/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        registry.addInterceptor(interceptor);
    }

    /*----------------------------------------------------------------------------------------
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="org.postgresql.Driver" />
        <property name="url" value="jdbc:postgresql://localhost:5432/playground" />
        <property name="username" value="postgres" />
        <property name="password" value="postgres" />
    </bean>
    -----------------------------------------------------------------------------------------*/
    @Bean(name="dataSource") // default
    public DataSource dbSource() {
        // TODO ConnectionPooling via BasicDataSource dataSource = new BasicDataSource();
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("db.driver"));
        dataSource.setUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.username"));
        dataSource.setPassword(env.getProperty("db.password"));
        // dataSource.setDefaultAutoCommit(false);
        return dataSource;

    }

    /* -------------------------------------------------------------------------------------------
    <bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource" />
    </bean>
    ---------------------------------------------------------------------------------------------*/
    @Bean(name="transactionManager") // default
    public PlatformTransactionManager txManager() {
        return new DataSourceTransactionManager(dbSource());
    }
    
    

}