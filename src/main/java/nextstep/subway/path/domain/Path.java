package nextstep.subway.path.domain;

import lombok.Getter;
import nextstep.subway.station.Station;

import java.util.Collections;
import java.util.List;

@Getter
public class Path {

    private final List<Station> stations;
    private final long distance;
    private final long duration;

    public Path(List<Station> stations, long distance, long duration) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(this.stations);
    }
}
