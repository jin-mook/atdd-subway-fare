package nextstep.subway.path.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.line.dto.LineStationsResponse;
import nextstep.subway.path.domain.Path;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PathResponse {

    private List<LineStationsResponse> stations;
    private long distance;
    private long duration;

    private PathResponse(List<LineStationsResponse> stations, long distance, long duration) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
    }

    public static PathResponse from(Path path) {
        List<LineStationsResponse> stations = path.getStations().stream()
                .map(LineStationsResponse::new)
                .collect(Collectors.toList());

        return new PathResponse(stations, path.getDistance(), path.getDuration());
    }

//    public static PathResponse from(List<Section> sections, long distance) {
//        List<LineStationsResponse> stations = sections.stream().flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
//                .distinct()
//                .map(LineStationsResponse::new)
//                .collect(Collectors.toList());
//
//        return new PathResponse(stations, distance);
//    }
}
