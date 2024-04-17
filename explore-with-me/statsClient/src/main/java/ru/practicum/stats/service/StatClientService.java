package ru.practicum.stats.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.stats.EndpointHitDto;
import ru.practicum.stats.ViewStatsDto;

import java.util.Map;


@Service

public class StatClientService {

    private final RestTemplate restTemplate;

    private final String addressBaseUrl;

    public StatClientService(@Value("${stat-server.url}") String addressBaseUrl) {
        this.addressBaseUrl = addressBaseUrl;
        this.restTemplate = new RestTemplate();
    }

    public void post(EndpointHitDto hitDto) {
        HttpEntity<EndpointHitDto> httpEntity = new HttpEntity<>(hitDto);
        restTemplate.exchange(addressBaseUrl + "/hit", HttpMethod.POST, httpEntity, Object.class);
    }

    public ResponseEntity<ViewStatsDto[]> getStats(String start, String end, String[] uris, boolean unique) {
        Map<String, Object> parameters;
        String path;
        if (uris != null) {
            parameters = Map.of(
                    "start", start,
                    "end", end,
                    "uris", uris,
                    "unique", unique
            );
            path = addressBaseUrl + "/stats/?start={start}&end={end}&uris={uris}&unique={unique}";
        } else {
            parameters = Map.of(
                    "start", start,
                    "end", end,
                    "unique", unique
            );
            path = addressBaseUrl + "/stats/?start={start}&end={end}&unique={unique}";
        }
        ResponseEntity<ViewStatsDto[]> serverResponse = restTemplate.getForEntity(path, ViewStatsDto[].class, parameters);
        if (serverResponse.getStatusCode().is2xxSuccessful()) {
            return serverResponse;
        }
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(serverResponse.getStatusCode());
        if (serverResponse.hasBody()) {
            return responseBuilder.body(serverResponse.getBody());
        }
        return responseBuilder.build();
    }
}
