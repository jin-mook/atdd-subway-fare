package nextstep.subway.path.domain.distancepolicy;

public interface DistancePaymentPolicy {

    boolean check(long distance);
    int applyPaymentPolicy(long distance);
}
