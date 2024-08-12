package nextstep.subway.path.domain;

import lombok.Getter;
import nextstep.subway.station.Station;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Getter
public class Path {

    private static final Set<PaymentPolicy> policies = Set.of(
            new DefaultPaymentPolicy(),
            new UnderFifthPaymentPolicy(),
            new OverFifthPaymentPolicy()
    );

    private final List<Station> stations;
    private final long distance;
    private final long duration;
    private final Payment payment;

    public Path(List<Station> stations, long distance, long duration) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.payment = new Payment(distance);
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(this.stations);
    }

    public int getPayment() {
        return this.payment.getPayment();
    }
}
