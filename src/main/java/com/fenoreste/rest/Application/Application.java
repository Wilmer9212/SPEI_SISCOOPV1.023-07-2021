package com.fenoreste.rest.Application;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

@ApplicationPath("api/v1.0")
public class Application extends ResourceConfig {
    public Application() {
        packages("com.fenoreste.spei.services");
        register(JacksonFeature.class);
        register(RolesAllowedDynamicFeature.class);
    }
}
