package nextstep.subway.exception;

import nextstep.subway.common.SubwayErrorMessage;

public class IllegalDurationValueException extends RuntimeException {

    public IllegalDurationValueException(SubwayErrorMessage subwayErrorMessage) {
        super(subwayErrorMessage.getMessage());
    }
}
