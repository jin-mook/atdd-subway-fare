package nextstep.subway.path.infrastructure;

import nextstep.subway.common.SubwayErrorMessage;
import nextstep.subway.exception.NotConnectedStationException;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.ShortestPathService;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationFixtures;
import nextstep.subway.station.StationRepository;
import nextstep.utils.AcceptanceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.LineAssuredTemplate.createLine;
import static nextstep.subway.line.SectionAssuredTemplate.addSection;
import static nextstep.subway.station.StationAssuredTemplate.createStationWithId;

class JgraphtShortestPathServiceTest extends AcceptanceTest {

    private long 논현역_id;
    private long 양재역_id;
    private List<Section> sections;

    @Autowired
    private ShortestPathService shortestPathService;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        long 강남역_id = createStationWithId(StationFixtures.강남역.getName());
        long 양재역_id = createStationWithId(StationFixtures.양재역.getName());
        long 논현역_id = createStationWithId(StationFixtures.논현역.getName());
        long 고속터미널역_id = createStationWithId(StationFixtures.고속터미널역.getName());
        long 교대역_id = createStationWithId(StationFixtures.교대역.getName());

        long 신분당선_id = createLine(new LineRequest("신분당선", "red", 논현역_id, 강남역_id, 4L)).then().extract().jsonPath().getLong("id");
        long 삼호선_id = createLine(new LineRequest("3호선", "orange", 논현역_id, 고속터미널역_id, 2L)).then().extract().jsonPath().getLong("id");

        addSection(신분당선_id, new SectionRequest(강남역_id, 양재역_id, 3L));
        addSection(삼호선_id, new SectionRequest(고속터미널역_id, 교대역_id, 1L));
        addSection(삼호선_id, new SectionRequest(교대역_id, 양재역_id, 3L));

        this.논현역_id = 논현역_id;
        this.양재역_id = 양재역_id;

        this.sections = lineRepository.findAllWithSectionsAndStations()
                .stream().flatMap(line -> line.getSections().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    @DisplayName("source 역과 target 역이 연결되어 있지 않다면 에러가 발생합니다.")
    @Test
    void notConnect() {
        // given
        long 사당역_id = createStationWithId(StationFixtures.사당역.getName());
        long 방배역_id = createStationWithId(StationFixtures.방배역.getName());
        createLine(new LineRequest("2호선", "green", 사당역_id, 방배역_id, 4L))
                .then().extract().jsonPath().getLong("id");

        Station sourceStation = stationRepository.findById(논현역_id).get();
        Station targetStation = stationRepository.findById(사당역_id).get();


        List<Section> sections = lineRepository.findAllWithSectionsAndStations()
                .stream().flatMap(line -> line.getSections().stream())
                .distinct()
                .collect(Collectors.toList());


        // when
        // then
        Assertions.assertThatThrownBy(() -> shortestPathService.findShortestPath(sections, sourceStation, targetStation))
                .isInstanceOf(NotConnectedStationException.class)
                .hasMessage(SubwayErrorMessage.NOT_CONNECTED_STATION.getMessage());
    }

    @DisplayName("source 역 id와 target 역 id 를 입력받으면 최단거리를 구간 목록을 전달합니다.")
    @Test
    void shortestSections() {
        // given
        // when
        Station sourceStation = stationRepository.findById(논현역_id).get();
        Station targetStation = stationRepository.findById(양재역_id).get();


        Path path = shortestPathService.findShortestPath(sections, sourceStation, targetStation);
        List<Station> stationList = path.getStations();

        // then
        Assertions.assertThat(stationList).hasSize(4)
                .extracting("name")
                .containsExactly(
                        StationFixtures.논현역.getName(),
                        StationFixtures.고속터미널역.getName(),
                        StationFixtures.교대역.getName(),
                        StationFixtures.양재역.getName()
                );
        Assertions.assertThat(path.getDistance()).isEqualTo(6);
    }
}