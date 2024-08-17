package nextstep.subway.path.domain.distancepolicy;

import static nextstep.subway.path.domain.PaymentPolicyConstant.*;

public class DefaultDistancePaymentPolicy implements DistancePaymentPolicy {

    @Override
    public boolean check(long distance) {
        return distance <= DEFAULT_DISTANCE && distance > 0;
    }

    @Override
    public int applyPaymentPolicy(long distance) {
        return DEFAULT_PAYMENT;
    }
}
