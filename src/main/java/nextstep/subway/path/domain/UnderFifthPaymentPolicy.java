package nextstep.subway.path.domain;

public class UnderFifthPaymentPolicy implements PaymentPolicy {

    private static final long DEFAULT_DISTANCE = 10;
    private static final long OVER_FARE_DISTANCE = 50;
    private static final int DEFAULT_PAYMENT = 1250;
    private static final int DISTANCE_RATE = 5;
    private static final int DEFAULT_DISTANCE_FEE = 100;

    @Override
    public boolean check(long distance) {
        return distance > DEFAULT_DISTANCE && distance <= OVER_FARE_DISTANCE;
    }

    @Override
    public int applyPaymentPolicy(long distance) {
        return DEFAULT_PAYMENT + calculateOverTenFare(distance);
    }

    private int calculateOverTenFare(long distance) {
        long restDistance = distance - DEFAULT_DISTANCE;
        int overCount = ((int) restDistance / DISTANCE_RATE) + 1;
        return overCount * DEFAULT_DISTANCE_FEE;
    }
}
