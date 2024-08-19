package ru.evg.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Setter
@Getter
@ConfigurationProperties(prefix = "client")
public class ClientProperties {

    private long checkTimeout;

    private List<String> urls;

}
