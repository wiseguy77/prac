package wise.study.prac.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wise.study.prac.dto.IssuedJwtResponse;
import wise.study.prac.dto.IssuedOtpResponse;
import wise.study.prac.dto.MfaRequest;
import wise.study.prac.security.form.CustomUserDetails;
import wise.study.prac.service.AuthenticationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping("login")
  public IssuedOtpResponse login(MfaRequest mfaRequest) {

    return authenticationService.logIn(mfaRequest);
  }

  @PostMapping("logout")
  public void logout(@AuthenticationPrincipal UserDetails user) {

    authenticationService.logOut(user.getUsername());
  }

  @PostMapping("otp")
  public IssuedJwtResponse verifyOtp(@AuthenticationPrincipal CustomUserDetails user) {

    return authenticationService.issueJwt(user.getUsername());
  }

  @GetMapping("jwt")
  public IssuedJwtResponse reissueJwt(@AuthenticationPrincipal CustomUserDetails user) {

    String account = user.getUsername();
    return authenticationService.issueJwt(account);
  }
}
