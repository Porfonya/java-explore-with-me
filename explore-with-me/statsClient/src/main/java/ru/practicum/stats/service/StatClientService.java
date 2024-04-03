package ru.practicum.stats.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import ru.practicum.stats.EndpointHitDto;
import ru.practicum.stats.ViewStatsDto;


@Service

public class StatClientService {

    private final RestTemplate restTemplate;

    private final String addressBaseUrl;

    public StatClientService(@Value("${explorer-with-me-server.url}") String addressBaseUrl, RestTemplateBuilder builder) {
        this.addressBaseUrl = addressBaseUrl;
        this.restTemplate = builder.rootUri(addressBaseUrl).build();
    }

    public Object post(@RequestBody EndpointHitDto hitDto) {
        return restTemplate.postForEntity(addressBaseUrl, hitDto, EndpointHitDto.class, "");
    }

    public Object getStats() {

        return restTemplate.getForEntity(addressBaseUrl, ViewStatsDto.class);

    }
}
