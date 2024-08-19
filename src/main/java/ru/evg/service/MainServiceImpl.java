package ru.evg.service;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import ru.evg.config.ClientProperties;

@Service
@AllArgsConstructor
public class MainServiceImpl implements CommandLineRunner {

    private final ClientProperties clientProperties;

    private final HealthCheckAggr healthCheckAggr;

    @Override
    public void run(String... args) throws InterruptedException {
        while (true) {
            healthCheckAggr.checkHealthServices();
            Thread.sleep(clientProperties.getCheckTimeout());
        }
    }

}
