package nextstep.auth.domain;

import nextstep.auth.application.dto.ApplicationTokenResponse;
import nextstep.auth.application.dto.ResourceResponse;

public interface OauthClient {

    ApplicationTokenResponse requestToken(String code);

    ResourceResponse requestResource(String accessToken);
}
