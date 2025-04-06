package wise.study.prac.security.token;

import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class LogInAuthToken extends UsernamePasswordAuthenticationToken {


  public LogInAuthToken(Object principal, Object credentials) {
    super(principal, credentials);
  }

  public LogInAuthToken(Object principal, Object credentials,
      Collection<? extends GrantedAuthority> authorities) {
    super(principal, credentials, authorities);
  }
}
