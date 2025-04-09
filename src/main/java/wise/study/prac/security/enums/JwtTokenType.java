package wise.study.prac.security.enums;

import lombok.Getter;
import org.springframework.http.HttpMethod;

@Getter
public enum JwtTokenType {

  ACCESS("access-token", null, null),
  REFRESH("refresh-token", HttpMethod.GET, "/api/auth/jwt");

  private final String value;
  private final HttpMethod httpMethod;
  private final String url;

  JwtTokenType(String value, HttpMethod httpMethod, String url) {
    this.value = value;
    this.httpMethod = httpMethod;
    this.url = url;
  }
}
