package wise.study.prac.security.enums;

import java.util.Arrays;
import java.util.Objects;
import lombok.Getter;
import org.springframework.http.HttpMethod;

@Getter
public enum JwtTokenType {

  ACCESS(null, null),
  REFRESH(HttpMethod.GET, "/api/auth/jwt");

  private final HttpMethod httpMethod;
  private final String url;

  JwtTokenType(HttpMethod httpMethod, String url) {
    this.httpMethod = httpMethod;
    this.url = url;
  }

  public static JwtTokenType findExpectedTokenType(HttpMethod httpMethod, String requestUrl) {

    return Arrays.stream(JwtTokenType.values())
        .filter((type) -> type.matches(httpMethod, requestUrl))
        .findFirst()
        .orElse(ACCESS);
  }

  private boolean matches(HttpMethod httpMethod, String url) {
    return Objects.equals(this.httpMethod, httpMethod) &&
        Objects.equals(this.url, url);
  }
}
