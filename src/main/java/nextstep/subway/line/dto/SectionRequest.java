package nextstep.subway.line.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SectionRequest {

    @NotNull
    private Long upStationId;
    @NotNull
    private Long downStationId;
    @NotNull
    private Long distance;
    @NotNull
    private Long duration;

    public SectionRequest(Long upStationId, Long downStationId, Long distance, Long duration) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.duration = duration;
    }
}
