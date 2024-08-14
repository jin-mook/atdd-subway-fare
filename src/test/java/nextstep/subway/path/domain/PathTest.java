package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationFixtures;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class PathTest {

    @DisplayName("추가요금이 있는 노선이 존재한다면 추가요금을 기본 요금에 추가요금을 더합니다.")
    @Test
    void addLineFee() {
        // given
        Station 사당역 = StationFixtures.사당역;
        Station 강남역 = StationFixtures.강남역;
        Station 양재역 = StationFixtures.양재역;

        Section 신분당구간 = Section.firstSection(강남역, 양재역, 30L, 10L);
        Section 이호선구간 = Section.firstSection(사당역, 강남역, 36L, 20L);

        new Line("신분당선", "red", 신분당구간, 900);
        new Line("2호선", "green", 이호선구간);
        // when
        Path path = new Path(List.of(신분당구간, 이호선구간), List.of(사당역, 강남역, 양재역));
        // then
        Assertions.assertThat(path.getPayment()).isEqualTo(2950);
    }
}