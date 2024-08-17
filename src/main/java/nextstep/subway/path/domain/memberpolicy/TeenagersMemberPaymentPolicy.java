package nextstep.subway.path.domain.memberpolicy;

import static nextstep.subway.path.domain.memberpolicy.MemberPaymentPolicyConstant.*;

public class TeenagersMemberPaymentPolicy implements MemberPaymentPolicy {

    @Override
    public boolean check(long age) {
        return age >= TEENAGERS_LOW_AGE && age < ADULT_LOW_AGE;
    }

    @Override
    public int applyPaymentPolicy(int currentPayment) {
        int disCountPayment = (int) ((currentPayment - DEFAULT_DISCOUNT_PAYMENT) * TEENAGER_DISCOUNT_RATE);
        return currentPayment - disCountPayment;
    }
}
