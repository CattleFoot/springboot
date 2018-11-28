package com.mugwort.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Autowired
    private ZuulProperties properties;

    public SwaggerResourcesProvider swaggerResourcesProvider() {
        return () -> {
            List<SwaggerResource> resources = new ArrayList<>();
            properties.getRoutes().values().stream().forEach(router ->
                    resources.add(createResource(router.getServiceId(), router.getServiceId(), "2.0")));
            return resources;
        };
    }

    private SwaggerResource createResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        String[] microService = location.split("-");
        if (null != microService && microService.length == 2) {
            swaggerResource.setLocation("/" + microService[0] + "/" + microService[1] + "/v2/api-docs");
        }
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;

    }
}
