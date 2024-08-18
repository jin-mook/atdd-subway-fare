package nextstep.subway.path.domain.memberpolicy;

public class DefaultMemberPaymentPolicy implements MemberPaymentPolicy {

    @Override
    public boolean check(long age) {
        return false;
    }

    @Override
    public int applyPaymentPolicy(int currentPayment) {
        return currentPayment;
    }
}
