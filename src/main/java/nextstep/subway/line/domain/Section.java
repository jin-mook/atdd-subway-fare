package nextstep.subway.line.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.common.SubwayErrorMessage;
import nextstep.subway.exception.IllegalDistanceValueException;
import nextstep.subway.exception.IllegalDurationValueException;
import nextstep.subway.exception.NotSameUpAndDownStationException;
import nextstep.subway.station.Station;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Section {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "line_id")
    private Line line;

    private Integer lineOrder;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    private Long distance;
    private Long duration;

    private Section(Line line, Integer lineOrder, Station upStation, Station downStation, Long distance, Long duration) {
        if (upStation.equals(downStation)) {
            throw new NotSameUpAndDownStationException(SubwayErrorMessage.NOT_SAME_UP_AND_DOWN_STATION);
        }
        if (distance <= 0) {
            throw new IllegalDistanceValueException(SubwayErrorMessage.ILLEGAL_DISTANCE_VALUE);
        }
        if (duration <= 0) {
            throw new IllegalDurationValueException(SubwayErrorMessage.ILLEGAL_DURATION_VALUE);
        }

        this.line = line;
        this.lineOrder = lineOrder;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.duration = duration;
    }

    public static Section firstSection(Station upStation, Station downStation, Long distance, Long duration) {
        return new Section(null, null, upStation, downStation, distance, duration);
    }

    public static Section joinSections(Section upSection, Section downSection) {
        return new Section(
                upSection.getLine(),
                upSection.getLineOrder(),
                upSection.getUpStation(),
                downSection.getDownStation(),
                upSection.distance + downSection.distance,
                upSection.duration + downSection.duration
        );
    }

    public boolean containStation(Station station) {
        return upStation.equals(station) || downStation.equals(station);
    }

    public void setMappingWithLine(Line line) {
        this.line = line;
    }

    public void decreaseDistance(Long distance) {
        if (this.distance <= distance) {
            throw new IllegalDistanceValueException(SubwayErrorMessage.LARGE_DISTANCE_THAN_CURRENT_SECTION);
        }

        this.distance -= distance;
    }

    public void decreaseDuration(Long duration) {
        if (this.duration <= duration) {
            throw new IllegalDurationValueException(SubwayErrorMessage.LARGE_DURATION_THAN_CURRENT_SECTION);
        }

        this.duration -= duration;
    }

    public void changeUpStation(Station station) {
        this.upStation = station;
    }

    public void changeDownStation(Station station) {
        this.downStation = station;
    }

    public void setFirstSectionOrder() {
        this.lineOrder = 1;
    }

    public void setOrderFrontSection(Section section) {
        this.lineOrder = section.getLineOrder();
    }

    public void setOrderBehindSection(Section section) {
        this.lineOrder = section.getLineOrder() + 1;
    }

    public void addOneOrder(Section section) {
        if (this.lineOrder >= section.lineOrder) {
            this.lineOrder ++;
        }
    }

    public void minusOneOrder(Section section) {
        if (this.lineOrder > section.lineOrder) {
            this.lineOrder--;
        }
    }

    public boolean isRightSection(Station upStation, Station downStation) {
        return this.upStation.equals(upStation) && this.downStation.equals(downStation);
    }

    public LineAdditionFee getLineAdditionFee() {
        return this.line.getAdditionFee();
    }
}
