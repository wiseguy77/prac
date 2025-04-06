package wise.study.prac.security.provider;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import wise.study.prac.security.form.CustomUserDetails;
import wise.study.prac.security.token.OtpAuthToken;

@Component
@RequiredArgsConstructor
public class OtpAuthProvider implements AuthenticationProvider {

  private final UserDetailsService userDetailsService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    String account = authentication.getName();
    String otpCode = authentication.getCredentials().toString();

    var userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(account);
    validateOtpCode(userDetails, otpCode);

    var authority = new SimpleGrantedAuthority(userDetails.getRole().name());

    return new OtpAuthToken(userDetails, otpCode, List.of(authority));
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return OtpAuthToken.class.isAssignableFrom(authentication);
  }

  private void validateOtpCode(CustomUserDetails userDetails, String otpCode) {

    if (!userDetails.hasValidOtpCode() || !(userDetails.getOtpCode().equals(otpCode))) {
      throw new BadCredentialsException("인증 실패");
    }
  }
}
