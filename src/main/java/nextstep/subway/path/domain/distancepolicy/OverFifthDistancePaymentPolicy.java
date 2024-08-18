package nextstep.subway.path.domain.distancepolicy;

import static nextstep.subway.path.domain.distancepolicy.DistancePaymentPolicyConstant.*;

public class OverFifthDistancePaymentPolicy implements DistancePaymentPolicy {

    @Override
    public boolean check(long distance) {
        return distance > OVER_FARE_DISTANCE;
    }

    @Override
    public int applyPaymentPolicy(long distance) {
        return DEFAULT_PAYMENT + calculateOverTenFare(distance);
    }

    private int calculateOverTenFare(long distance) {
        long restDistance = distance - DEFAULT_DISTANCE;
        int overCount = ((int) restDistance / OVER_FIFTH_DISTANCE_RATE) + 1;
        return overCount * DEFAULT_DISTANCE_FEE;
    }
}
