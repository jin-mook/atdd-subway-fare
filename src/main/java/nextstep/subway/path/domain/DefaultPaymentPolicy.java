package nextstep.subway.path.domain;

import static nextstep.subway.path.domain.PaymentPolicyConstant.*;

public class DefaultPaymentPolicy implements PaymentPolicy {

    @Override
    public boolean check(long distance) {
        return distance <= DEFAULT_DISTANCE && distance > 0;
    }

    @Override
    public int applyPaymentPolicy(long distance) {
        return DEFAULT_PAYMENT;
    }
}
