package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.common.SubwayErrorMessage;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.StationFixtures;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.springframework.http.HttpStatus;

import static nextstep.subway.line.LineAssuredTemplate.createLine;
import static nextstep.subway.line.SectionAssuredTemplate.addSection;
import static nextstep.subway.path.PathAssuredTemplate.searchShortestPath;
import static nextstep.subway.station.StationAssuredTemplate.createStationWithId;

public class PathStepDef implements En {

    private long 논현역_id;
    private long 양재역_id;
    private long 고속터미널역_id;
    private long 교대역_id;
    private long 사당역_id;
    private ExtractableResponse<Response> response;


    public PathStepDef() {
        Given("필요한 역과, 구간, 노선을 등록합니다.", () -> {
            long 강남역_id = createStationWithId(StationFixtures.강남역.getName());
            this.양재역_id = createStationWithId(StationFixtures.양재역.getName());
            this.논현역_id = createStationWithId(StationFixtures.논현역.getName());
            this.고속터미널역_id = createStationWithId(StationFixtures.고속터미널역.getName());
            this.교대역_id = createStationWithId(StationFixtures.교대역.getName());
            this.사당역_id = createStationWithId(StationFixtures.사당역.getName());

            long 신분당선_id = createLine(new LineRequest("신분당선", "green", 논현역_id, 강남역_id, 4L, 10L)).then().extract().jsonPath().getLong("id");
            addSection(신분당선_id, new SectionRequest(강남역_id, 양재역_id, 3L, 20L));

            long 삼호선_id = createLine(new LineRequest("3호선", "orange", 논현역_id, 고속터미널역_id, 2L, 20L)).then().extract().jsonPath().getLong("id");
            addSection(삼호선_id, new SectionRequest(고속터미널역_id, 교대역_id, 1L, 15L));
            addSection(삼호선_id, new SectionRequest(교대역_id, 양재역_id, 3L, 10L));
        });

        When("논현역에서 양재역으로 갈 수 있는 길을 조회합니다.", () -> {
            this.response = searchShortestPath(논현역_id, 양재역_id)
                    .then().log().all().extract();
        });

        Then("논현역부터 양재역까지의 가장 빠른 길의 역들과 총 거리를 응답받습니다.", () -> {
            Assertions.assertThat(response.jsonPath().getList("stations")).hasSize(4)
                    .extracting("id", "name")
                    .contains(
                            Tuple.tuple((int) 논현역_id, StationFixtures.논현역.getName()),
                            Tuple.tuple((int) 고속터미널역_id, StationFixtures.고속터미널역.getName()),
                            Tuple.tuple((int) 교대역_id, StationFixtures.교대역.getName()),
                            Tuple.tuple((int) 양재역_id, StationFixtures.양재역.getName())
                    );

            Assertions.assertThat(response.jsonPath().getLong("distance")).isEqualTo(6);
        });

        When("서로 연결되어 있지 않은 역의 최단거리를 요청합니다.", () -> {
            response = searchShortestPath(사당역_id, 양재역_id)
                    .then().log().all()
                    .extract();
        });

        Then("연결되어 있지 않다는 에러 응답을 전달받습니다.", () -> {
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            Assertions.assertThat(response.body().asString()).isEqualTo(SubwayErrorMessage.NOT_CONNECTED_STATION.getMessage());
        });

        When("존재하지 않은 역의 최단거리를 요청합니다.", () -> {
            response = searchShortestPath(Long.MAX_VALUE, 양재역_id)
                    .then().log().all()
                    .extract();
        });

        Then("존재하지 않는 역이라는 에러 응답을 전달받습니다.", () -> {
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            Assertions.assertThat(response.body().asString()).isEqualTo(SubwayErrorMessage.NO_STATION_EXIST.getMessage());
        });
    }
}
