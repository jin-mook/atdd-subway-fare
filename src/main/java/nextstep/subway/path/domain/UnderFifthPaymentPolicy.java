package nextstep.subway.path.domain;

import static nextstep.subway.path.domain.PaymentPolicyConstant.*;

public class UnderFifthPaymentPolicy implements PaymentPolicy {

    @Override
    public boolean check(long distance) {
        return distance > DEFAULT_DISTANCE && distance <= OVER_FARE_DISTANCE;
    }

    @Override
    public int applyPaymentPolicy(long distance) {
        return DEFAULT_PAYMENT + calculateOverTenFare(distance);
    }

    private int calculateOverTenFare(long distance) {
        long restDistance = distance - DEFAULT_DISTANCE;
        int overCount = ((int) restDistance / UNDER_FIFTH_DISTANCE_RATE) + 1;
        return overCount * DEFAULT_DISTANCE_FEE;
    }
}
