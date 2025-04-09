package wise.study.prac.security.enums;

import static java.util.Collections.unmodifiableMap;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum JwtHeader {

  TYP("typ", "JWT"), ALG("alg", "HS256");

  private static final Map<String, Object> HEADERS;
  final String value;

  static {
    Map<String, Object> map = Stream.of(JwtHeader.values())
        .collect(Collectors.toMap(JwtHeader::getKey, (JwtHeader::getValue)));

    HEADERS = unmodifiableMap(map);
  }

  final String key;

  JwtHeader(String key, String value) {

    this.key = key;
    this.value = value;
  }

  public static Map<String, Object> getHeaders() {
    return HEADERS;
  }
}
