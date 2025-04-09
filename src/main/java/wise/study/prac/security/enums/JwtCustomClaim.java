package wise.study.prac.security.enums;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;

@Getter
public enum JwtCustomClaim {

  ID("id"),
  ACCOUNT("account"),
  ROLE("role"),
  JWT("jwt"),
  TOKEN_TYPE("token_type"),
  TTL("ttl"),
  ISSUED_AT("issued_at"),
  EXPIRES_AT("expires_at");

  final String key;

  JwtCustomClaim(String claimKey) {
    this.key = claimKey;
  }

  public static Optional<JwtCustomClaim> fromKey(String key) {
    return Arrays.stream(values())
        .filter(c -> c.key.equalsIgnoreCase(key))
        .findFirst();
  }
}
