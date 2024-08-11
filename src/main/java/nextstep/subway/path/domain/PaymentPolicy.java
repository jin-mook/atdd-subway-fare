package nextstep.subway.path.domain;

public interface PaymentPolicy {

    boolean check(long distance);
    int applyPaymentPolicy(long distance);
}
