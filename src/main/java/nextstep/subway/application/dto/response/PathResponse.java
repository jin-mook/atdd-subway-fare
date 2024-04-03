package nextstep.subway.application.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.entity.Path;
import nextstep.subway.domain.entity.Station;

@NoArgsConstructor
@Getter
public class PathResponse {

    @NoArgsConstructor
    @Getter
    public static class StationDto {

        private Long id;

        private String name;

        public StationDto(Station station) {
            this.id = station.getId();
            this.name = station.getName();
        }

    }

    private List<StationDto> stations;
    private long distance;

    public PathResponse(Path shortestPath) {
        this.stations = shortestPath.getStations().stream()
            .map(StationDto::new)
            .collect(Collectors.toList());
        this.distance = (long) shortestPath.getDistance();

    }


}
