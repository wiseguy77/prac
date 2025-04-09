package wise.study.prac.security.provider;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import wise.study.prac.security.token.LogInAuthToken;

@Component
@RequiredArgsConstructor
public class LogInAuthProvider implements AuthenticationProvider {

  private final UserDetailsService userDetailsService;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    String userName = authentication.getName();
    String password = authentication.getCredentials().toString();

    UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

    if (comparePassword(password, userDetails.getPassword())) {
      return new LogInAuthToken(userDetails, password, List.of());
    } else {
      throw new BadCredentialsException("인증 실패");
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return LogInAuthToken.class.isAssignableFrom(authentication);
  }

  private boolean comparePassword(String inputPassword, String savedPassword) {

    return passwordEncoder.matches(inputPassword, savedPassword);
  }
}
