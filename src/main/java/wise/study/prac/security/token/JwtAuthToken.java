package wise.study.prac.security.token;

import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import wise.study.prac.security.enums.JwtTokenType;

public class JwtAuthToken extends UsernamePasswordAuthenticationToken {

  private final JwtTokenType jwtTokenType;

  public JwtAuthToken(JwtTokenType jwtTokenType, Object principal, Object credentials) {
    super(principal, credentials);
    this.jwtTokenType = jwtTokenType;
  }

  public JwtAuthToken(JwtTokenType jwtTokenType, Object principal, Object credentials,
      Collection<? extends GrantedAuthority> authorities) {
    super(principal, credentials, authorities);
    this.jwtTokenType = jwtTokenType;
  }

  public JwtTokenType getJwtTokenType() {
    return jwtTokenType;
  }
}
