package wise.study.prac.security.token;

import java.util.Collection;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class JwtAuthToken extends UsernamePasswordAuthenticationToken {

  private final String accessToken;
  private final String refreshToken;

  public JwtAuthToken(String access, String refresh) {
    super(null, null, null);
    this.accessToken = access;
    this.refreshToken = refresh;
  }

  public JwtAuthToken(String access, String refresh, Object principal,
      Collection<? extends GrantedAuthority> authorities) {
    super(principal, null, authorities);
    this.accessToken = access;
    this.refreshToken = refresh;
  }

  public boolean hasRefreshToken() {
    return this.refreshToken != null;
  }
}
