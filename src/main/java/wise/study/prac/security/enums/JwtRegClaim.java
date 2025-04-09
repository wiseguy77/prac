package wise.study.prac.security.enums;

import static java.util.Collections.unmodifiableMap;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum JwtRegClaim {
  ISS("iss", ""), SUB("sub", ""),
  AUD("aud", ""), EXP("exp", ""),
  NBF("nbf", ""), IAT("iat", ""),
  JTI("jti", "");

  private static final Map<String, Object> REG_CLAIMS;
  final String value;

  static {
    Map<String, Object> map = Stream.of(JwtRegClaim.values())
        .collect(Collectors.toMap((JwtRegClaim::getKey), (JwtRegClaim::getValue)));

    REG_CLAIMS = unmodifiableMap(map);
  }

  final String key;

  JwtRegClaim(String key, String value) {

    this.key = key;
    this.value = value;
  }

  public static Map<String, Object> getRegClaims() {
    return REG_CLAIMS;
  }
}
