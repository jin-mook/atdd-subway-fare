package nextstep.subway.line.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Getter
@Embeddable
public class LineAdditionFee {

    private final int additionFee;

    public LineAdditionFee(int additionFee) {
        this.additionFee = additionFee;
    }

    protected LineAdditionFee() {
        this.additionFee = 0;
    }
}
