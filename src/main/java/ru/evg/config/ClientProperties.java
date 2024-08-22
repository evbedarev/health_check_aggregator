package ru.evg.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "client")
public class ClientProperties {

    @Setter
    @Getter
    private long checkTimeout;

    @Setter
    @Getter
    private List<String> urls;

}
