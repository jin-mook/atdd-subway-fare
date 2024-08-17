package nextstep.subway.path.domain.memberpolicy;

public class TeenagersMemberPaymentPolicy implements MemberPaymentPolicy {


    @Override
    public boolean check(long age) {
        return age >= 13 && age < 19;
    }

    @Override
    public int applyPaymentPolicy(int currentPayment) {
        int disCountPayment = (int) ((currentPayment - 350) * 0.2);
        return currentPayment - disCountPayment;
    }
}
