package nextstep.subway.path.domain;

import lombok.Getter;
import nextstep.subway.common.SubwayErrorMessage;
import nextstep.subway.exception.IllegalDistanceValueException;
import nextstep.subway.line.domain.LineAdditionFee;

import java.util.Set;

@Getter
public class Payment {

    private static final Set<PaymentPolicy> policies = Set.of(
            new DefaultPaymentPolicy(),
            new UnderFifthPaymentPolicy(),
            new OverFifthPaymentPolicy()
    );

    private final int payment;

    private Payment(long distance) {
        this.payment = setPayment(distance);
    }

    private Payment(int payment) {
        this.payment = payment;
    }

    private int setPayment(long distance) {
        PaymentPolicy paymentPolicy = policies.stream().filter(policy -> policy.check(distance))
                .findFirst()
                .orElseThrow(() -> new IllegalDistanceValueException(SubwayErrorMessage.ILLEGAL_PATH_DISTANCE_VALUE));

        return paymentPolicy.applyPaymentPolicy(distance);
    }

    public static Payment of(long distance) {
        return new Payment(distance);
    }

    public Payment addLineAdditionFee(LineAdditionFee lineAdditionFee) {
        return new Payment(this.payment + lineAdditionFee.getAdditionFee());
    }
}
