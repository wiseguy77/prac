package wise.study.prac.security.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import wise.study.prac.security.form.CustomUserDetailsImpl;
import wise.study.prac.security.token.OtpAuthToken;

@Component
@RequiredArgsConstructor
public class OtpAuthProvider implements AuthenticationProvider {

  private final UserDetailsService userDetailsService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    String account = authentication.getName();
    String otpCode = authentication.getCredentials().toString();

    var userDetails = (CustomUserDetailsImpl) userDetailsService.loadUserByUsername(account);
    validateOtpCode(userDetails, otpCode);

    return new OtpAuthToken(userDetails, otpCode, userDetails.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return OtpAuthToken.class.isAssignableFrom(authentication);
  }

  private void validateOtpCode(CustomUserDetailsImpl userDetails, String otpCode) {

    if (!userDetails.hasValidOtpCode() || !(userDetails.getOtpCode().equals(otpCode))) {
      throw new BadCredentialsException("인증 실패");
    }
  }
}
