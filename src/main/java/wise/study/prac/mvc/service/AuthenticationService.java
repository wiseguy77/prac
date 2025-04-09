package wise.study.prac.mvc.service;

import static wise.study.prac.mvc.exception.ErrorCode.JWT_INVALID;
import static wise.study.prac.mvc.exception.ErrorCode.UNAUTHORIZED;
import static wise.study.prac.mvc.exception.ErrorCode.USER_INACTIVE_STATUS;
import static wise.study.prac.mvc.exception.ErrorCode.USER_NOT_FOUND;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wise.study.prac.mvc.dto.IssuedJwtResponse;
import wise.study.prac.mvc.dto.IssuedOtpResponse;
import wise.study.prac.mvc.dto.MfaRequest;
import wise.study.prac.mvc.entity.Member;
import wise.study.prac.mvc.exception.ErrorCode;
import wise.study.prac.mvc.exception.PracException;
import wise.study.prac.mvc.repository.JwtRepository;
import wise.study.prac.mvc.repository.MemberRepository;
import wise.study.prac.security.exception.PracAuthenticationException;
import wise.study.prac.security.jwt.JwtIssuedInfo;
import wise.study.prac.security.jwt.JwtUserDetails;
import wise.study.prac.security.jwt.JwtUtil;
import wise.study.prac.security.otp.OtpClientProxy;
import wise.study.prac.security.token.JwtAuthToken;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
  private final JwtUtil jwtUtil;
  private final OtpClientProxy otpClient;
  private final JwtRepository jwtRepository;
  private final ObjectMapper objectMapper;

  private final long accessTtl = Duration.ofMinutes(10).toMillis();
  private final long refreshTtl = Duration.ofDays(7).toMillis();

  @Transactional
  public IssuedOtpResponse logIn(MfaRequest request) {

    Member member = memberRepository.findMemberByAccount(request.getAccount())
        .orElseThrow(() -> new UsernameNotFoundException("로그인 실패"));

    boolean validPassword = passwordEncoder.matches(request.getPassword(), member.getPassword());

    if (validPassword) {

      String sentOtp = otpClient.sendOtp(member.getPhoneNumber());
      LocalDateTime otpExpiryTime = LocalDateTime.now().plusDays(1);

      member.setOtpCode(sentOtp);
      member.setOtpExpiryTime(otpExpiryTime);
      memberRepository.save(member);

      return IssuedOtpResponse.builder()
          .account(member.getAccount())
          .otpCode(sentOtp).build();
    } else {
      throw new PracException(UNAUTHORIZED);
    }
  }

  public void logOut() {

    JwtAuthToken jwtAuthToken = validateJwt();
    invalidateJwt(jwtAuthToken);
  }

  @Transactional
  public IssuedJwtResponse reissueJwt(String account) {

    JwtAuthToken jwtAuthToken = validateJwt();
    IssuedJwtResponse response = issueJwt(account);
    invalidateJwt(jwtAuthToken);

    return response;
  }

  @Transactional
  public IssuedJwtResponse issueJwt(String account) {

    Member member = memberRepository.findMemberByAccount(account)
        .orElseThrow(() -> new PracException(USER_NOT_FOUND));

    if (!member.isActive()) {
      throw new PracException(USER_INACTIVE_STATUS);
    }

    JwtIssuedInfo issuedAccessInfo = jwtUtil.issueAccessJwt(member, accessTtl);
    JwtIssuedInfo issuedRefreshInfo = jwtUtil.issueRefreshJwt(member, refreshTtl);

    jwtRepository.save(issuedAccessInfo);
    jwtRepository.save(issuedRefreshInfo);

    return IssuedJwtResponse.builder()
        .accessToken(issuedAccessInfo.getJwt())
        .refreshToken(issuedRefreshInfo.getJwt()).build();
  }

  public JwtUserDetails authenticateJwt(JwtAuthToken jwtAuthToken) {

    String jwt = jwtAuthToken.hasRefreshToken() ? jwtAuthToken.getRefreshToken()
        : jwtAuthToken.getAccessToken();

    JwtUserDetails jwtUserDetails = jwtRepository.find(jwt);
    jwtUtil.validate(jwt, jwtUserDetails.getSecretKey());

    return jwtUserDetails;
  }

  public void invalidateJwt(JwtAuthToken jwtAuthToken) {

    jwtRepository.delete(jwtAuthToken.getAccessToken());
    jwtRepository.delete(jwtAuthToken.getRefreshToken());
  }

  private JwtAuthToken validateJwt() {

    Authentication authentication = getAuthentication();

    if (!(authentication instanceof JwtAuthToken jwtAuthToken)) {
      throw new PracAuthenticationException(JWT_INVALID);
    }

    String accessToken = jwtAuthToken.getAccessToken();
    String refreshToken = jwtAuthToken.getRefreshToken();

    if (accessToken == null || refreshToken == null) {
      throw new PracAuthenticationException(ErrorCode.JWT_MISSING);
    }

    return jwtAuthToken;
  }

  private Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }
}
