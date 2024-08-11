package nextstep.subway.path.domain;

import lombok.Getter;
import nextstep.subway.station.Station;

import java.util.Collections;
import java.util.List;

@Getter
public class Path {

    private static final long DEFAULT_DISTANCE = 10;
    private static final long OVER_FARE_DISTANCE = 50;
    private static final int DEFAULT_PAYMENT = 1250;

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
        int paymentTotal = DEFAULT_PAYMENT;

        if (distance > DEFAULT_DISTANCE && distance <= OVER_FARE_DISTANCE) {
            paymentTotal += calculateOverTenFare(distance);
        }

        if (distance > OVER_FARE_DISTANCE) {
            paymentTotal += calculateOverFifthFare(distance);
        }
        this.payment = paymentTotal;
    }

    private int calculateOverTenFare(long distance) {
        long restDistance = distance - DEFAULT_DISTANCE;
        int overCount = ((int) restDistance / 5) + 1;
        return overCount * 100;
    }

    private int calculateOverFifthFare(long distance) {
        long restDistance = distance - DEFAULT_DISTANCE;
        int overCount = ((int) restDistance / 8) + 1;
        return overCount * 100;
    }
}
