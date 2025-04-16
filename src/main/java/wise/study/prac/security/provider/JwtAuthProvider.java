package wise.study.prac.security.provider;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import wise.study.prac.biz.service.AuthenticationService;
import wise.study.prac.security.handler.CustomAuthenticationEntryPoint;
import wise.study.prac.security.jwt.JwtUserDetails;
import wise.study.prac.security.token.JwtAuthToken;

@Component
@RequiredArgsConstructor
public class JwtAuthProvider implements AuthenticationProvider {

  private final AuthenticationService authenticationService;
  private final CustomAuthenticationEntryPoint authenticationEntryPoint;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    JwtAuthToken jwtAuthToken = (JwtAuthToken) authentication;

    JwtUserDetails jwtUserDetails = authenticationService.authenticateJwt(jwtAuthToken);
    String accessToken = jwtAuthToken.getAccessToken();
    String refreshToken = jwtAuthToken.getRefreshToken();
    List<GrantedAuthority> authorities = List.of(
        new SimpleGrantedAuthority(jwtUserDetails.getRole().name()));

    return new JwtAuthToken(accessToken, refreshToken, jwtUserDetails, authorities);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return JwtAuthToken.class.isAssignableFrom(authentication);
  }
}
