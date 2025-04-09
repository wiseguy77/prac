package wise.study.prac.security.jwt;

import java.util.Map;

public interface JwtInfo {

  Map<String, Object> getClaims();
  String getSecretKey();
}
