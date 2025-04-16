package wise.study.prac.biz.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wise.study.prac.biz.dto.IssuedJwtResponse;
import wise.study.prac.biz.dto.IssuedOtpResponse;
import wise.study.prac.biz.dto.MfaRequest;
import wise.study.prac.biz.service.AuthenticationService;
import wise.study.prac.security.form.CustomUserDetailsImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @Operation(summary = "사용자 로그인", description = "계정과 비밀번호로 로그인 후 OTP를 발급받습니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = IssuedOtpResponse.class))),
      @ApiResponse(responseCode = "500", description = "로그인 실패", content = @Content(mediaType = "application/json", schema = @Schema()))})
  @PostMapping("login")
  public IssuedOtpResponse logIn(MfaRequest mfaRequest) {

    return authenticationService.logIn(mfaRequest);
  }

  @Operation(summary = "사용자 로그아웃", description = "로그아웃 후 발급한 JWT를 모두 무효화합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = @Content(mediaType = "application/json", schema = @Schema())),
      @ApiResponse(responseCode = "500", description = "로그아웃 실패")})
  @PostMapping("logout")
  public void logOut() {

    authenticationService.logOut();
  }

  @Operation(summary = "사용자 OTP 인증", description = "발급받은 OTP 코드를 인증하여 JWT를 발급받습니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OTP 인증 성공 후 JWT 발급", content = @Content(mediaType = "application/json", schema = @Schema(implementation = IssuedJwtResponse.class))),
      @ApiResponse(responseCode = "500", description = "OTP 인증 실패", content = @Content(mediaType = "application/json", schema = @Schema()))})
  @PostMapping("otp")
  public IssuedJwtResponse verifyOtp(@AuthenticationPrincipal CustomUserDetailsImpl user) {

    return authenticationService.issueJwt(user.getUsername());
  }

  @Operation(summary = "사용자 JWT 갱신", description = "만료된 accessToken과 refreshToken으로 새로운 JWT를 발급합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "JWT 재발급 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = IssuedJwtResponse.class))),
      @ApiResponse(responseCode = "500", description = "JWT 재발급 실패", content = @Content(mediaType = "application/json", schema = @Schema()))})
  @GetMapping("jwt")
  public IssuedJwtResponse reissueJwt(@AuthenticationPrincipal CustomUserDetailsImpl user) {

    return authenticationService.reissueJwt(user.getUsername());
  }
}
