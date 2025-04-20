package wise.study.prac.common.util;

import static wise.study.prac.biz.exception.ErrorCode.JSON_CONVERT;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import wise.study.prac.biz.exception.PracException;

@RequiredArgsConstructor
public class JsonUtil {

  private final ObjectMapper objectMapper;

  public String toJson(Object value) {
    try {
      return objectMapper.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      throw new PracException(JSON_CONVERT);
    }
  }

  public <T> T fromJson(String json, Class<T> clazz) {
    try {
      return objectMapper.readValue(json, clazz);
    } catch (JsonProcessingException e) {
      throw new PracException(JSON_CONVERT);
    }
  }

  public <T> T fromJson(String json, TypeReference<T> typeRef) {
    try {
      return objectMapper.readValue(json, typeRef);
    } catch (JsonProcessingException e) {
      throw new PracException(JSON_CONVERT);
    }
  }
}
