package ru.evg.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClient;
import ru.evg.config.ClientProperties;
import ru.evg.dto.HealthDto;
import ru.evg.dto.HealthErrorDto;
import ru.evg.indicator.CustomHealthIndicator;

import java.util.*;

@Service
public class WebClientServiceImpl implements HealthCheckAggr {

    private static final Logger logger = LoggerFactory.getLogger(MainServiceImpl.class);

    private final ClientProperties clientProperties;

    private final CustomHealthIndicator customHealthIndicator;

    private Map<WebClient, String> webClientsMap = new HashMap<>();

    public WebClientServiceImpl(ClientProperties clientProperties, CustomHealthIndicator customHealthIndicator) {
        this.clientProperties = clientProperties;
        this.customHealthIndicator = customHealthIndicator;
        for (String url: clientProperties.getUrls()) {
            webClientsMap.put(WebClient.builder().baseUrl(url).build(), url);
        }
    }

    private Optional<HealthDto> checkHealthCheck(WebClient webClient) {
        try {
            return Optional.ofNullable(webClient.get().retrieve().bodyToMono(HealthDto.class).block());
        } catch (WebClientRequestException exception) {
            System.out.println(exception.getMessage());
            return Optional.of(new HealthDto("ERROR"));
        }
    }

    @Override
    public void checkHealthServices() {
        List<HealthErrorDto> checkList = new ArrayList<>();
        for (Map.Entry<WebClient, String> entry: webClientsMap.entrySet()) {
            Optional<HealthDto> healthDto = checkHealthCheck(entry.getKey());
            if (healthDto.isPresent()) {
                checkList.add(new HealthErrorDto(healthDto.get().getStatus(), entry.getValue()));
            } else {
                checkList.add(new HealthErrorDto("ERROR", entry.getValue()));
            }
        }
        setIndicatorStatus(checkList);
    }

    private void setIndicatorStatus(List<HealthErrorDto> errorDtos) {
        boolean hasErrors = false;
        for (HealthErrorDto errorDto: errorDtos) {
            if (HealthErrorDto.hasErrors(errorDto))  {
                hasErrors = true;
                logger.error("ERROR get health check from url: %s".formatted(errorDto.getUrl()));
            } else {
                logger.info("Get success health check from url: %s".formatted(errorDto.getUrl()));
            }
        }
        if (hasErrors)
            customHealthIndicator.setHasError(true);
        else
            customHealthIndicator.setHasError(false);
    }
}
