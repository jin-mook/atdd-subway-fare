package nextstep.subway.domain;

import nextstep.subway.domain.policy.DefaultFare;
import nextstep.subway.domain.policy.ElevenToFiftyExtraFare;
import nextstep.subway.domain.policy.FareManager;
import nextstep.subway.domain.policy.OverFiftyExtraFare;
import org.junit.jupiter.api.BeforeEach;

public abstract class FareManagerLoaderTest {

    @BeforeEach
    void setUp() {
        FareManager.clearPolicy();
        FareManager.addPolicy(new DefaultFare());
        FareManager.addPolicy(new ElevenToFiftyExtraFare());
        FareManager.addPolicy(new OverFiftyExtraFare());
    }
}
