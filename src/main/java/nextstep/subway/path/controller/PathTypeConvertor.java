package nextstep.subway.path.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.path.domain.PathType;
import org.springframework.core.convert.converter.Converter;

public class PathTypeConvertor implements Converter<String, PathType> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public PathType convert(String source) {
        try {
            String pathType = objectMapper.readValue(source, String.class);
            return PathType.valueOf(pathType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
