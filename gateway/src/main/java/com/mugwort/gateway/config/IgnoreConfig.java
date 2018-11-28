package com.mugwort.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "filter")
public class IgnoreConfig {
    private String ignores;

    public List<String> items() {
        List<String> ignoreFilter = null;
        if (null != ignores && ignores.length() > 0) {
            ignoreFilter = Arrays.asList(ignores.trim());
        }
        return ignoreFilter;
    }
}
