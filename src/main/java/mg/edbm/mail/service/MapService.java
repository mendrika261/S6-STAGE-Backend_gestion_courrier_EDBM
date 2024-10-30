package mg.edbm.mail.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import mg.edbm.mail.dto.MapDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Service
@Getter
@Setter
public class MapService {
    @Value("${map.service.url}")
    private String mapServiceUrl;
    private final Duration TIMEOUT = Duration.ofSeconds(60);
    private final Double TIME_MULTIPLIER_PERCENT = 3.5;

    public MapDto getDirections(Double startLat, Double startLong, Double endLat, Double endLong) {
        try {
            final JsonNode jsonNode =
                    WebClient
                            .create(mapServiceUrl)
                            .get()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/directions/driving-car")
                                    .queryParam("start", startLong + "," + startLat)
                                    .queryParam("end", endLong + "," + endLat)
                                    .build())
                            .retrieve()
                            .bodyToMono(JsonNode.class)
                            .block(TIMEOUT);
            return new MapDto(jsonNode, TIME_MULTIPLIER_PERCENT);
        } catch (Exception ignored) {
            return new MapDto();
        }
    }
}
