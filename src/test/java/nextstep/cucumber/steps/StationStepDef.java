package nextstep.cucumber.steps;

import static nextstep.subway.acceptance.step.StationSteps.지하철_역_생성_요청;
import static nextstep.subway.acceptance.step.StationSteps.지하철역_목록_응답에서_역_이름_목록_추출;
import static nextstep.subway.acceptance.step.StationSteps.지하철역_목록_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextstep.cucumber.AcceptanceContext;
import nextstep.subway.application.dto.response.StationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class StationStepDef implements En {


    @Autowired
    private AcceptanceContext context;

    public StationStepDef() {

        Given("지하철역들을 생성 요청하고", (DataTable table) -> {
            List<Map<String, Object>> maps = table.asMaps(String.class, Object.class);
            maps.forEach(param -> {
                ExtractableResponse<Response> response = 지하철_역_생성_요청(param);
                context.store.put(param.get("name").toString(), response.as(StationResponse.class));
            });
        });

        When("지하철역을 생성하면", () -> {
            Map<String, Object> params = new HashMap<>();
            params.put("name", "강남역");
            context.response = 지하철_역_생성_요청(params);
        });

        Then("지하철역이 생성된다", () -> {
            assertThat(context.response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        });

        Then("지하철역 목록 조회 시 생성한 역을 찾을 수 있다", () -> {
            List<String> stationNames = 지하철역_목록_응답에서_역_이름_목록_추출(지하철역_목록_조회_요청());
            assertThat(stationNames).containsAnyOf("강남역");
        });
    }

}
