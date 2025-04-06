package wise.study.prac.service;

import io.jsonwebtoken.Claims;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import wise.study.prac.dto.IssuedJwtResponse;
import wise.study.prac.dto.IssuedOtpResponse;
import wise.study.prac.dto.MfaRequest;
import wise.study.prac.repository.MemberRepository;
import wise.study.prac.util.JwtUtil;
import wise.study.prac.entity.Member;
import wise.study.prac.security.OtpClientProxy;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
  private final JwtUtil jwtUtil;
  private final OtpClientProxy otpClient;

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
      throw new BadCredentialsException("로그인 실패");
    }
  }

  @Transactional
  public void logOut(String account) {

    Member member = memberRepository.findMemberByAccount(account)
        .orElseThrow(() -> new UsernameNotFoundException("인증 실패"));

    member.resetJwt();
    memberRepository.save(member);
  }

  @Transactional
  public IssuedJwtResponse issueJwt(String account) {

    Member member = memberRepository.findMemberByAccount(account)
        .orElseThrow(() -> new UsernameNotFoundException("인증 실패"));


    if (!StringUtils.hasText(member.getSecretKey())) {
      member.setSecretKey(UUID.randomUUID().toString());
    }
    String accessToken = jwtUtil.createAccessToken(member);
    String refreshToken = jwtUtil.createRefreshToken(member);

    member.resetOtp();
    member.setAccessToken(accessToken);
    member.setRefreshToken(refreshToken);
    memberRepository.save(member);

    return IssuedJwtResponse.builder().accessToken(accessToken).refreshToken(refreshToken)
        .build();
  }

  public Claims validateAccessToken(String jwt) throws NotFoundException {

    Member member = memberRepository.findMemberByAccessToken(jwt)
        .orElseThrow(NotFoundException::new);

    return jwtUtil.validateToken(jwt, member.getSecretKey());
  }

  public Claims validateRefreshToken(String jwt) throws NotFoundException {
    Member member = memberRepository.findMemberByRefreshToken(jwt)
        .orElseThrow(NotFoundException::new);

    return jwtUtil.validateToken(jwt, member.getSecretKey());
  }
}
