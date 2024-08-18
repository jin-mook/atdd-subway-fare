package nextstep.subway.path.domain;

import lombok.Getter;
import nextstep.subway.common.SubwayErrorMessage;
import nextstep.subway.exception.IllegalDistanceValueException;
import nextstep.subway.line.domain.LineAdditionFee;
import nextstep.subway.path.domain.distancepolicy.DefaultDistancePaymentPolicy;
import nextstep.subway.path.domain.distancepolicy.DistancePaymentPolicy;
import nextstep.subway.path.domain.distancepolicy.OverFifthDistancePaymentPolicy;
import nextstep.subway.path.domain.distancepolicy.UnderFifthDistancePaymentPolicy;
import nextstep.subway.path.domain.memberpolicy.ChildMemberPaymentPolicy;
import nextstep.subway.path.domain.memberpolicy.DefaultMemberPaymentPolicy;
import nextstep.subway.path.domain.memberpolicy.MemberPaymentPolicy;
import nextstep.subway.path.domain.memberpolicy.TeenagersMemberPaymentPolicy;

import java.util.Set;

@Getter
public class Payment {

    private static final Set<DistancePaymentPolicy> distancePolicies = Set.of(
            new DefaultDistancePaymentPolicy(),
            new UnderFifthDistancePaymentPolicy(),
            new OverFifthDistancePaymentPolicy()
    );

    private static final Set<MemberPaymentPolicy> memberPolicies = Set.of(
            new DefaultMemberPaymentPolicy(),
            new ChildMemberPaymentPolicy(),
            new TeenagersMemberPaymentPolicy()
    );

    private final int payment;

    private Payment(long distance) {
        this.payment = setPayment(distance);
    }

    private Payment(int payment) {
        this.payment = payment;
    }

    private int setPayment(long distance) {
        DistancePaymentPolicy distancePaymentPolicy = distancePolicies.stream().filter(policy -> policy.check(distance))
                .findFirst()
                .orElseThrow(() -> new IllegalDistanceValueException(SubwayErrorMessage.ILLEGAL_PATH_DISTANCE_VALUE));

        return distancePaymentPolicy.applyPaymentPolicy(distance);
    }

    public static Payment of(long distance) {
        return new Payment(distance);
    }

    public Payment addLineAdditionFee(LineAdditionFee lineAdditionFee) {
        return new Payment(this.payment + lineAdditionFee.getAdditionFee());
    }

    public Payment applyMemberAgeFee(int age) {
        MemberPaymentPolicy memberPaymentPolicy = memberPolicies.stream().filter(policy -> policy.check(age))
                .findFirst()
                .orElse(new DefaultMemberPaymentPolicy());

        int payment = memberPaymentPolicy.applyPaymentPolicy(this.payment);
        return new Payment(payment);
    }
}
