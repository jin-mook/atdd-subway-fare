package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.acceptance.OauthAssuredTemplate;
import nextstep.member.acceptance.test.GithubUser;
import nextstep.subway.common.SubwayErrorMessage;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.path.PathAssuredTemplate;
import nextstep.subway.path.domain.PathType;
import nextstep.subway.station.StationFixtures;
import org.assertj.core.groups.Tuple;
import org.springframework.http.HttpStatus;

import static nextstep.subway.line.LineAssuredTemplate.createLine;
import static nextstep.subway.line.SectionAssuredTemplate.addSection;
import static nextstep.subway.path.PathAssuredTemplate.로그인_없이_경로_조회;
import static nextstep.subway.station.StationAssuredTemplate.createStationWithId;
import static org.assertj.core.api.Assertions.assertThat;

public class PathStepDef implements En {

    private long 강남역_id;
    private long 논현역_id;
    private long 양재역_id;
    private long 고속터미널역_id;
    private long 교대역_id;
    private long 사당역_id;

    private long 신분당선_id;
    private long 삼호선_id;

    private String accessToken;

    private ExtractableResponse<Response> response;


    public PathStepDef() {
        Given("필요한 역과, 구간, 노선을 등록합니다.", () -> {
            this.강남역_id = createStationWithId(StationFixtures.강남역.getName());
            this.양재역_id = createStationWithId(StationFixtures.양재역.getName());
            this.논현역_id = createStationWithId(StationFixtures.논현역.getName());
            this.고속터미널역_id = createStationWithId(StationFixtures.고속터미널역.getName());
            this.교대역_id = createStationWithId(StationFixtures.교대역.getName());
            this.사당역_id = createStationWithId(StationFixtures.사당역.getName());
        });

        And("노선을 등록할 때 신분당선은 추가요금 900원을 적용합니다.", () -> {
            this.신분당선_id = createLine(new LineRequest("신분당선", "green", 논현역_id, 강남역_id, 10L, 10L)).then().extract().jsonPath().getLong("id");
            addSection(신분당선_id, new SectionRequest(강남역_id, 양재역_id, 20L, 20L));
        });

        And("노선을 등록할 때 삼호선은 추가요금 500원을 적용합니다.", () -> {
            this.삼호선_id = createLine(new LineRequest("3호선", "orange", 논현역_id, 고속터미널역_id, 10L, 20L)).then().extract().jsonPath().getLong("id");
            addSection(삼호선_id, new SectionRequest(고속터미널역_id, 교대역_id, 10L, 15L));
            addSection(삼호선_id, new SectionRequest(교대역_id, 양재역_id, 5L, 10L));
        });


        When("논현역에서 양재역으로 갈 수 있는 길을 거리 기준으로 조회합니다.", () -> {
            this.response = 로그인_없이_경로_조회(논현역_id, 양재역_id, PathType.DISTANCE)
                    .then().log().all().extract();
        });

        Then("논현역부터 양재역까지의 거리가 가장 빠른 길의 역들과 총 거리, 소요 시간을 응답받습니다.", () -> {
            assertThat(response.jsonPath().getList("stations")).hasSize(4)
                    .extracting("id", "name")
                    .contains(
                            Tuple.tuple((int) 논현역_id, StationFixtures.논현역.getName()),
                            Tuple.tuple((int) 고속터미널역_id, StationFixtures.고속터미널역.getName()),
                            Tuple.tuple((int) 교대역_id, StationFixtures.교대역.getName()),
                            Tuple.tuple((int) 양재역_id, StationFixtures.양재역.getName())
                    );

            assertThat(response.jsonPath().getLong("distance")).isEqualTo(25);
            assertThat(response.jsonPath().getLong("duration")).isEqualTo(45);
        });

        And("거리 기준 지하철 경로 조회에 이용 요금도 함께 응답합니다.", () -> {
//            assertThat(response.jsonPath().getInt("payment")).isEqualTo(1650);
            assertThat(response.jsonPath().getInt("payment")).isEqualTo(2150);
        });

        When("서로 연결되어 있지 않은 역의 최단거리를 요청합니다.", () -> {
            response = 로그인_없이_경로_조회(사당역_id, 양재역_id, PathType.DISTANCE)
                    .then().log().all()
                    .extract();
        });

        Then("연결되어 있지 않다는 에러 응답을 전달받습니다.", () -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(response.body().asString()).isEqualTo(SubwayErrorMessage.NOT_CONNECTED_STATION.getMessage());
        });

        When("존재하지 않은 역의 최단거리를 요청합니다.", () -> {
            response = 로그인_없이_경로_조회(Long.MAX_VALUE, 양재역_id, PathType.DISTANCE)
                    .then().log().all()
                    .extract();
        });

        Then("존재하지 않는 역이라는 에러 응답을 전달받습니다.", () -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(response.body().asString()).isEqualTo(SubwayErrorMessage.NO_STATION_EXIST.getMessage());
        });

        When("논현역에서 양재역으로 갈 수 있는 길을 소요 시간 기준으로 조회합니다.", () -> {
            response = 로그인_없이_경로_조회(논현역_id, 양재역_id, PathType.DURATION)
                    .then().log().all().extract();
        });

        Then("논현역부터 양재역까지의 소요 시간이 가장 빠른 길의 역들과 총 거리, 소요 시간을 응답받습니다.", () -> {
            assertThat(response.jsonPath().getList("stations")).hasSize(3)
                    .extracting("id", "name")
                    .contains(
                            Tuple.tuple((int) 논현역_id, StationFixtures.논현역.getName()),
                            Tuple.tuple((int) 강남역_id, StationFixtures.강남역.getName()),
                            Tuple.tuple((int) 양재역_id, StationFixtures.양재역.getName())
                    );

            assertThat(response.jsonPath().getLong("distance")).isEqualTo(30);
            assertThat(response.jsonPath().getLong("duration")).isEqualTo(30);
        });

        And("소요 시간 기준 지하철 경로 조회에 이용 요금도 함께 응답합니다.", () -> {
//            assertThat(response.jsonPath().getInt("payment")).isEqualTo(1750);
            assertThat(response.jsonPath().getInt("payment")).isEqualTo(2650);
        });

        Given("{int} 살 사용자가 로그인 합니다.", (Integer age) -> {
            GithubUser githubUser = GithubUser.findUserByAge(age);
            this.accessToken = OauthAssuredTemplate.깃허브로그인(githubUser.getCode())
                    .then().extract().jsonPath().getString("accessToken");
        });

        When("로그인 토큰과 함께 논현역에서 양재역으로 갈 수 있는 길을 거리 기준으로 조회합니다.", () -> {
            response = PathAssuredTemplate.로그인_이후_경로_조회(논현역_id, 양재역_id, PathType.DISTANCE, accessToken)
                    .then().log().all().extract();
        });

        When("로그인 토큰과 함께 논현역에서 양재역으로 갈 수 있는 길을 소요 시간 기준으로 조회합니다.", () -> {
            response = PathAssuredTemplate.로그인_이후_경로_조회(논현역_id, 양재역_id, PathType.DURATION, accessToken)
                    .then().log().all().extract();
        });

        And("나이 기준으로 최종 이용 요금인 {int} 가격도 함께 응답합니다.", (Integer payment) -> {
            assertThat(response.jsonPath().getInt("payment")).isEqualTo(payment);
        });
    }
}
