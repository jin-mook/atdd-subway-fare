package nextstep.subway.path.domain.memberpolicy;

public interface MemberPaymentPolicy {

    boolean check(long age);
    int applyPaymentPolicy(int currentPayment);
}
