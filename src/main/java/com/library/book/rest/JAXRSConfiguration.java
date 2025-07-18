package com.library.book.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;
import java.util.HashSet;

@ApplicationPath("/")
public class JAXRSConfiguration extends Application {
    
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(BookResource.class);
        classes.add(CorsFilter.class);
        return classes;
    }
}
