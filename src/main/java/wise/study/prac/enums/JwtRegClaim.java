package wise.study.prac.enums;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum JwtRegClaim {
  ISS(""), SUB(""), AUD(""),
  EXP(""), NBF(""), IAT(""), JTI("");

  final String value;
  JwtRegClaim(String value) {
    this.value = value;
  }

  public static Map<String, Object> getRegClaims() {
    return Stream.of(JwtRegClaim.values())
        .collect(Collectors.toMap((Enum::name), (JwtRegClaim::getValue)));
  }
}
