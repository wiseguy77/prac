package wise.study.prac.enums;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum JwtHeader {

  TYP("jwt"), ALG("hs256");

  final String value;

  JwtHeader(String value) {
    this.value = value;
  }

  public static Map<String, Object> getHeaders() {
    return Stream.of(JwtHeader.values())
        .collect(Collectors.toMap((Enum::name), (JwtHeader::getValue)));
  }
}
