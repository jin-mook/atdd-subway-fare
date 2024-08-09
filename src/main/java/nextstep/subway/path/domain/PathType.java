package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;

public enum PathType {
    DISTANCE {
        @Override
        public Long getWeight(Section section) {
            return section.getDistance();
        }
    },
    DURATION {
        @Override
        public Long getWeight(Section section) {
            return section.getDuration();
        }
    };

    public abstract Long getWeight(Section section);
}
