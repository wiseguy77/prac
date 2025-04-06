package wise.study.prac.enums;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum JwtCustomClaim {

  ACCOUNT(""), ROLE("");

  final String value;
  JwtCustomClaim(String value) {
    this.value = value;
  }

  public static Map<String, Object> getRegClaims() {
    return Stream.of(JwtCustomClaim.values())
        .collect(Collectors.toMap((Enum::name), (JwtCustomClaim::getValue)));
  }
}
