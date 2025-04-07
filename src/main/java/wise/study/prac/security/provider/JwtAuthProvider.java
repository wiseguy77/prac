package wise.study.prac.security.provider;

import static wise.study.prac.enums.JwtCustomClaim.ACCOUNT;
import static wise.study.prac.enums.JwtCustomClaim.ROLE;
import static wise.study.prac.security.enums.JwtTokenType.REFRESH;

import io.jsonwebtoken.Claims;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import wise.study.prac.security.enums.JwtTokenType;
import wise.study.prac.security.form.CustomUserDetails;
import wise.study.prac.security.handler.CustomAuthenticationEntryPoint;
import wise.study.prac.security.token.JwtAuthToken;
import wise.study.prac.service.AuthenticationService;

@Component
@RequiredArgsConstructor
public class JwtAuthProvider implements AuthenticationProvider {

  private final AuthenticationService authenticationService;
  private final CustomAuthenticationEntryPoint authenticationEntryPoint;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    JwtTokenType jwtTokenType = ((JwtAuthToken) authentication).getJwtTokenType();
    String jwt = authentication.getCredentials().toString();

    Claims claims;

    if (jwtTokenType == REFRESH) {
      claims = authenticationService.validateRefreshToken(jwt);
    } else {
      claims = authenticationService.validateAccessToken(jwt);
    }

    String account = (String) claims.get(ACCOUNT.name());
    String role = (String) claims.get(ROLE.name());
    GrantedAuthority authority = new SimpleGrantedAuthority(role);

    return new JwtAuthToken(jwtTokenType, new CustomUserDetails(account), null, List.of(authority));
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return JwtAuthToken.class.isAssignableFrom(authentication);
  }
}
