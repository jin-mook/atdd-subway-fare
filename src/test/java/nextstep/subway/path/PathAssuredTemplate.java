package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import nextstep.subway.path.domain.PathType;
import org.springframework.http.MediaType;

public class PathAssuredTemplate {

    public static Response 로그인_없이_경로_조회(Long sourceStationId, Long targetStationId, PathType pathType) {

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .queryParam("source", sourceStationId)
                .queryParam("target", targetStationId)
                .queryParam("type", pathType)
                .get("/paths");
    }

    public static Response 로그인_이후_경로_조회(Long sourceStationId, Long targetStationId, PathType pathType, String accessToken) {

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .queryParam("source", sourceStationId)
                .queryParam("target", targetStationId)
                .queryParam("type", pathType)
                .get("/paths");
    }
}
