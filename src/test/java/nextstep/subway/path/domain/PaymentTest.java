package nextstep.subway.path.domain;

import nextstep.subway.common.SubwayErrorMessage;
import nextstep.subway.exception.IllegalDistanceValueException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaymentTest {

    @DisplayName("10km 이내 거리면 기본비용인 1250원 요금입니다.")
    @Test
    void basic() {
        // given
        long distance = 10;
        Payment payment = new Payment(distance);
        // when
        // then
        assertThat(payment.getPayment()).isEqualTo(1250);
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
    void tenDistanceOver(long distance, int paymentResult) {
        // given
        // when
        Payment payment = new Payment(distance);
        // then
        assertThat(payment.getPayment()).isEqualTo(paymentResult);
    }

    @ParameterizedTest(name = "50km 초과 거리는 8km 마다 추가요금 100원이 추가됩니다.")
    @CsvSource({
            "51, 1850",
            "58, 1950",
            "66, 2050"
    })
    void fifthDistanceOver(long distance, int paymentResult) {
        // given
        // when
        Payment payment = new Payment(distance);
        // then
        assertThat(payment.getPayment()).isEqualTo(paymentResult);
    }

    @DisplayName("잘못된 거리가 계산되면 에러가 발생합니다.")
    @Test
    void IllegalDistance() {
        // given
        // when
        // then
        assertThatThrownBy(() -> new Payment(0))
                .isInstanceOf(IllegalDistanceValueException.class)
                .hasMessage(SubwayErrorMessage.ILLEGAL_PATH_DISTANCE_VALUE.getMessage());
    }

}