package wise.study.prac.security.jwt;

import java.util.Date;
import wise.study.prac.security.enums.JwtTokenType;
import wise.study.prac.security.enums.RoleType;

public interface JwtUserDetails {

  long getId();

  String getAccount();

  RoleType getRole();

  String getSecretKey();

  long getTtl();

  Date getIssuedAt();

  Date getExpiresAt();

  JwtTokenType getTokenType();

  String getJwtHash();
}
