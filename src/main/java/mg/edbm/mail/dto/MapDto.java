package mg.edbm.mail.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class MapDto {
    private Double distance = 0.0;
    private Double duration = 0.0;
    private List<Double[]> coordinates = new ArrayList<>();

    public MapDto(JsonNode jsonNode, Double timeMultiplierPercent) {
        setDuration(jsonNode.get("features").get(0).get("properties").get("summary").get("duration").asDouble() * timeMultiplierPercent);
        setDistance(jsonNode.get("features").get(0).get("properties").get("summary").get("distance").asDouble());
        setCoordinates(jsonNode.get("features").get(0).get("geometry").get("coordinates"));
    }

    public void setCoordinates(JsonNode coordinates) {
        for (JsonNode coordinate : coordinates) {
            Double[] coord = new Double[2];
            coord[0] = coordinate.get(0).asDouble();
            coord[1] = coordinate.get(1).asDouble();
            this.getCoordinates().add(coord);
        }
    }
}
