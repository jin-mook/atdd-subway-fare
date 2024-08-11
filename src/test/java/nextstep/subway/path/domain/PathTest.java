package nextstep.subway.path.domain;

import nextstep.subway.station.Station;
import nextstep.subway.station.StationFixtures;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

class PathTest {

    private final List<Station> defaultStation = List.of(StationFixtures.FIRST_UP_STATION, StationFixtures.FIRST_DOWN_STATION);
    private final long duration = 10;

    @DisplayName("10km 이내 거리면 기본비용인 1250원 요금입니다.")
    @Test
    void basic() {
        // given
        long distance = 10;
        Path path = new Path(defaultStation, distance, duration);
        // when
        path.setPayment();
        // then
        Assertions.assertThat(path.getPayment()).isEqualTo(1250);
    }

    @ParameterizedTest(name = "10km 초과 50km 이하의 거리는 5km 마다 추가요금 100원이 추가됩니다.")
    @CsvSource({
            "12, 1350",
            "15, 1450",
            "20, 1550",
            "25, 1650",
            "30, 1750",
            "35, 1850",
            "38, 1850",
            "40, 1950",
            "45, 2050",
            "50, 2150"
    })
    void tenDistanceOver(long distance, int payment) {
        // given
        Path path = new Path(defaultStation, distance, duration);
        // when
        path.setPayment();
        // then
        Assertions.assertThat(path.getPayment()).isEqualTo(payment);
    }

    @ParameterizedTest(name = "50km 초과 거리는 8km 마다 추가요금 100원이 추가됩니다.")
    @CsvSource({
            "51, 1850",
            "58, 1950",
            "66, 2050"
    })
    void fifthDistanceOver(long distance, int payment) {
        // given
        Path path = new Path(defaultStation, distance, duration);
        // when
        path.setPayment();
        // then
        Assertions.assertThat(path.getPayment()).isEqualTo(payment);
    }
}