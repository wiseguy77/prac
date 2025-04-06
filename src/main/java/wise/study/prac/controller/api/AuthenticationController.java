package wise.study.prac.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wise.study.prac.dto.IssuedJwtResponse;
import wise.study.prac.dto.MfaRequest;
import wise.study.prac.dto.IssuedOtpResponse;
import wise.study.prac.service.AuthenticationService;
import wise.study.prac.security.form.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping("login")
  public ResponseEntity<IssuedOtpResponse> login(MfaRequest mfaRequest) {

    return ResponseEntity.ok(authenticationService.logIn(mfaRequest));
  }

  @PostMapping("logout")
  public ResponseEntity<Void> logout(@AuthenticationPrincipal UserDetails user) {

    authenticationService.logOut(user.getUsername());

    return ResponseEntity.ok().build();
  }

  @PostMapping("otp")
  public ResponseEntity<IssuedJwtResponse> verifyOtp(@AuthenticationPrincipal CustomUserDetails user) {

    return ResponseEntity.ok(authenticationService.issueJwt(user.getUsername()));
  }

  @GetMapping("jwt")
  public ResponseEntity<IssuedJwtResponse> reissueJwt(@AuthenticationPrincipal CustomUserDetails user) {

    String account = user.getUsername();
    return ResponseEntity.ok(authenticationService.issueJwt(account));
  }
}
