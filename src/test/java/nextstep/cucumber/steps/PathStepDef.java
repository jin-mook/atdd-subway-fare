package nextstep.cucumber.steps;

import static nextstep.subway.acceptance.step.PathSteps.지하철_경로_조회_요청;
import static nextstep.subway.acceptance.step.PathSteps.지하철역_경로_조회_응답에서_경로_거리_추출;
import static nextstep.subway.acceptance.step.PathSteps.지하철역_경로_조회_응답에서_역_이름_목록_추출;
import static nextstep.subway.acceptance.step.SectionSteps.지하철_구간_등록_요청;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextstep.cucumber.AcceptanceContext;
import nextstep.subway.application.dto.response.LineResponse;
import nextstep.subway.application.dto.response.StationResponse;
import org.assertj.core.api.SoftAssertions;
import org.springframework.beans.factory.annotation.Autowired;

public class PathStepDef implements En {

    @Autowired
    private AcceptanceContext context;


    public PathStepDef() {

        Given("지하철 구간을 등록 요청하고", (DataTable table) -> {
            List<Map<String, String>> maps = table.asMaps();
            maps.forEach(param -> {
                String lineName = param.get("lineName");
                Map<String, Object> params = new HashMap<>();
                params.put("upStationId",
                    ((StationResponse) context.store.get(param.get("upStation"))).getId().toString());
                params.put("downStationId",
                    ((StationResponse) context.store.get(param.get("downStation"))).getId().toString());
                params.put("distance", param.get("distance"));
                LineResponse line = (LineResponse) context.store.get(lineName);
                지하철_구간_등록_요청(line.getId(), params);
            });
        });

        When("{string}과 {string}의 경로를 조회하면", (String sourceStationName, String targetStationName) -> {
            Long sourceId = ((StationResponse) context.store.get(sourceStationName)).getId();
            Long targetId = ((StationResponse) context.store.get(targetStationName)).getId();
            context.response = 지하철_경로_조회_요청(sourceId, targetId);
        });

        Then("거리가 {long}인 {string}경로가 조회된다", (Long distance, String path) -> {
            List<String> stationNames = Arrays.asList(path.split(","));
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(지하철역_경로_조회_응답에서_역_이름_목록_추출(context.response))
                    .containsExactlyElementsOf(stationNames);
                softAssertions.assertThat(지하철역_경로_조회_응답에서_경로_거리_추출(context.response))
                    .isEqualTo(distance);
            });
        });
    }

}
