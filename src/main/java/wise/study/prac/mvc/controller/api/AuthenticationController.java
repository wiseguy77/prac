package wise.study.prac.mvc.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wise.study.prac.mvc.dto.IssuedJwtResponse;
import wise.study.prac.mvc.dto.IssuedOtpResponse;
import wise.study.prac.mvc.dto.MfaRequest;
import wise.study.prac.mvc.service.AuthenticationService;
import wise.study.prac.security.form.CustomUserDetailsImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping("login")
  public IssuedOtpResponse logIn(MfaRequest mfaRequest) {

    return authenticationService.logIn(mfaRequest);
  }

  @PostMapping("logout")
  public void logOut() {

    authenticationService.logOut();
  }

  @PostMapping("otp")
  public IssuedJwtResponse verifyOtp(@AuthenticationPrincipal CustomUserDetailsImpl user) {

    return authenticationService.issueJwt(user.getUsername());
  }

  @GetMapping("jwt")
  public IssuedJwtResponse reissueJwt(@AuthenticationPrincipal CustomUserDetailsImpl user) {

    return authenticationService.reissueJwt(user.getUsername());
  }
}
