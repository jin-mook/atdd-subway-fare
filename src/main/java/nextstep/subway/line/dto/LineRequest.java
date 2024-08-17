package nextstep.subway.line.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LineRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String color;

    @NotNull
    private long upStationId;

    @NotNull
    private long downStationId;

    @NotNull
    private long distance;

    @NotNull
    private long duration;

    @NotNull
    private int additionalFee;

    public LineRequest(String name, String color, long upStationId, long downStationId, long distance, long duration) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.duration = duration;
    }

    public LineRequest(String name, String color, long upStationId, long downStationId, long distance, long duration, int additionalFee) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.duration = duration;
        this.additionalFee = additionalFee;
    }
}
