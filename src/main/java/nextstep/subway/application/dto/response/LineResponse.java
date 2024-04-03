package nextstep.subway.application.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.entity.Line;
import nextstep.subway.domain.entity.Station;

@NoArgsConstructor
@Getter
public class LineResponse {


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

    private Long id;

    private String name;

    private String color;

    private List<StationDto> stations;

    public LineResponse(Line line) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        this.stations = line.getAllStations().stream()
            .map(StationDto::new)
            .collect(Collectors.toList());
    }


}
