package nextstep.subway.path.dto;

import lombok.Getter;
import nextstep.subway.path.domain.PathType;
import nextstep.subway.station.Station;

@Getter
public class SearchPathInfo {

    private Station sourceStation;
    private Station targetStation;
    private PathType pathType;

    public SearchPathInfo(Station sourceStation, Station targetStation, PathType pathType) {
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.pathType = pathType;
    }
}
