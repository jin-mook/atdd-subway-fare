package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.SearchPathInfo;

import java.util.List;

public interface ShortestPathService {

    Path findShortestPath(List<Section> sections, SearchPathInfo searchPathInfo);
}
