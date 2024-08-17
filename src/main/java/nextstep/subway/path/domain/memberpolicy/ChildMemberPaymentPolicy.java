package nextstep.subway.path.domain.memberpolicy;

public class ChildMemberPaymentPolicy implements MemberPaymentPolicy {

    @Override
    public boolean check(long age) {
        return age >= 6 && age < 13;
    }

    @Override
    public int applyPaymentPolicy(int currentPayment) {
        int disCountPayment = (int) ((currentPayment - 350) * 0.5);
        return currentPayment - disCountPayment;
    }
}
