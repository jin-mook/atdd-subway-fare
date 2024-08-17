package nextstep.subway.path.domain;

import lombok.Getter;
import nextstep.subway.line.domain.LineAdditionFee;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.Station;
import org.springframework.data.util.Pair;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Getter
public class Path {

    private final List<Section> sections;
    private final List<Station> stations;
    private final long distance;
    private final long duration;
    private Payment payment;

    public Path(List<Section> sections, List<Station> stations) {
        this.sections = sections;
        this.stations = stations;

        Pair<Long, Long> shortestDistanceAndDuration = findShortestDistanceAndDuration(sections);

        this.distance = shortestDistanceAndDuration.getFirst();
        this.duration = shortestDistanceAndDuration.getSecond();

        this.payment = Payment.of(distance).addLineAdditionFee(getMaxLineAdditionFee());
    }

    public void applyMemberAgeFee(int age) {
        this.payment = payment.applyMemberAgeFee(age);
    }

    private Pair<Long, Long> findShortestDistanceAndDuration(List<Section> sections) {
        return sections.stream()
                .map(section -> Pair.of(section.getDistance(), section.getDuration()))
                .reduce(Pair.of(0L, 0L), (subPair, currentPair) -> Pair.of(
                        subPair.getFirst() + currentPair.getFirst(),
                        subPair.getSecond() + currentPair.getSecond())
                );
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(this.stations);
    }

    public int getPayment() {
        return this.payment.getPayment();
    }

    private LineAdditionFee getMaxLineAdditionFee() {
        return sections.stream().map(Section::getLineAdditionFee)
                .max(Comparator.comparingInt(LineAdditionFee::getAdditionFee))
                .orElse(new LineAdditionFee(0));
    }
}
