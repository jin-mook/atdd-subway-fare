package nextstep.subway.path.controller;

import lombok.RequiredArgsConstructor;
import nextstep.common.SuccessResponse;
import nextstep.subway.path.domain.PathType;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.service.PathService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/paths")
@RestController
@RequiredArgsConstructor
public class PathController {

    private final PathService pathService;

    @GetMapping
    public ResponseEntity<PathResponse> getShortestPath(
            @RequestParam("source") Long sourceStationId,
            @RequestParam("target") Long targetStationId,
            @RequestParam(value = "type", defaultValue = "DISTANCE") PathType pathType
    ) {
        PathResponse data = pathService.findShortestPath(sourceStationId, targetStationId, pathType);
        return SuccessResponse.ok(data);
    }
}
