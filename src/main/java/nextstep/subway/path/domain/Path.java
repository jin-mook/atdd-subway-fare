package nextstep.subway.path.domain;

import lombok.Getter;
import nextstep.subway.common.SubwayErrorMessage;
import nextstep.subway.exception.IllegalDistanceValueException;
import nextstep.subway.station.Station;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Getter
public class Path {

    private static final long DEFAULT_DISTANCE = 10;
    private static final long OVER_FARE_DISTANCE = 50;
    private static final int DEFAULT_PAYMENT = 1250;

    private static final Set<PaymentPolicy> policies = Set.of(
            new DefaultPaymentPolicy(),
            new UnderFifthPaymentPolicy(),
            new OverFifthPaymentPolicy()
    );

    private final List<Station> stations;
    private final long distance;
    private final long duration;
    private int payment;

    public Path(List<Station> stations, long distance, long duration) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(this.stations);
    }

    public void setPayment() {
        PaymentPolicy paymentPolicy = policies.stream().filter(policy -> policy.check(distance))
                .findFirst()
                .orElseThrow(() -> new IllegalDistanceValueException(SubwayErrorMessage.ILLEGAL_PATH_DISTANCE_VALUE));

        this.payment = paymentPolicy.applyPaymentPolicy(distance);
    }
}
