package nextstep.subway.path.domain;

public class DefaultPaymentPolicy implements PaymentPolicy {

    private static final long DEFAULT_DISTANCE = 10;
    private static final int DEFAULT_PAYMENT = 1250;

    @Override
    public boolean check(long distance) {
        return distance <= DEFAULT_DISTANCE && distance > 0;
    }

    @Override
    public int applyPaymentPolicy(long distance) {
        return DEFAULT_PAYMENT;
    }
}
