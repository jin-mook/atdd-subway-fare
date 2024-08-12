package nextstep.subway.path.domain;

import lombok.Getter;
import nextstep.subway.common.SubwayErrorMessage;
import nextstep.subway.exception.IllegalDistanceValueException;

import java.util.Set;

@Getter
public class Payment {

    private static final Set<PaymentPolicy> policies = Set.of(
            new DefaultPaymentPolicy(),
            new UnderFifthPaymentPolicy(),
            new OverFifthPaymentPolicy()
    );

    private final int payment;

    public Payment(long distance) {
        this.payment = setPayment(distance);
    }

    private int setPayment(long distance) {
        PaymentPolicy paymentPolicy = policies.stream().filter(policy -> policy.check(distance))
                .findFirst()
                .orElseThrow(() -> new IllegalDistanceValueException(SubwayErrorMessage.ILLEGAL_PATH_DISTANCE_VALUE));

        return paymentPolicy.applyPaymentPolicy(distance);
    }
}
