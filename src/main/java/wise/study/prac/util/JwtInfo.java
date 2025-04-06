package wise.study.prac.util;

import java.util.Map;

public interface JwtInfo {

  Map<String, Object> getClaims();
  String getSecretKey();
}
