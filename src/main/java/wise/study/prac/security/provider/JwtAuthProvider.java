package wise.study.prac.security.provider;

import static wise.study.prac.enums.JwtCustomClaim.ACCOUNT;
import static wise.study.prac.enums.JwtCustomClaim.ROLE;

import io.jsonwebtoken.Claims;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import wise.study.prac.service.AuthenticationService;
import wise.study.prac.security.form.CustomUserDetails;
import wise.study.prac.security.token.JwtAuthToken;

@Component
@RequiredArgsConstructor
public class JwtAuthProvider implements AuthenticationProvider {

  private final AuthenticationService authenticationService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    boolean isRefresh = (boolean) authentication.getPrincipal();
    String jwt = authentication.getCredentials().toString();

    try {
      Claims claims;

      if(isRefresh) {
        claims = authenticationService.validateRefreshToken(jwt);
      } else {
        claims = authenticationService.validateAccessToken(jwt);
      }

      String account = (String) claims.get(ACCOUNT.name());
      String role = (String) claims.get(ROLE.name());
      GrantedAuthority authority = new SimpleGrantedAuthority(role);

      return new JwtAuthToken(new CustomUserDetails(account), null, List.of(authority));
    } catch (NotFoundException e) {
      throw new BadCredentialsException(e.getMessage());
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return JwtAuthToken.class.isAssignableFrom(authentication);
  }
}
