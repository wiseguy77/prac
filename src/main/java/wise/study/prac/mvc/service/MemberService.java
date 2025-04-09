package wise.study.prac.mvc.service;

import static wise.study.prac.mvc.exception.ErrorCode.KEY_GENERATION_FAIL;
import static wise.study.prac.mvc.exception.ErrorCode.PASSWORD_ENCRYPTION_FAIL;
import static wise.study.prac.mvc.exception.ErrorCode.REGISTER_MEMBER_FAIL;

import java.security.NoSuchAlgorithmException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wise.study.prac.mvc.dto.MemberInfoAllResponse;
import wise.study.prac.mvc.dto.MemberInfoResponse;
import wise.study.prac.mvc.dto.RegisterMemberRequest;
import wise.study.prac.mvc.entity.Member;
import wise.study.prac.mvc.exception.PracException;
import wise.study.prac.mvc.repository.MemberRepository;
import wise.study.prac.security.jwt.JwtUtil;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
  private final JwtUtil jwtUtil;

  @Transactional
  public void registerMember(RegisterMemberRequest request) {

    try {
      Member newMember = Member.builder()
          .account(request.getAccount())
          .password(passwordEncoder.encode(request.getPassword()))
          .secretKey(jwtUtil.generateSecretKey())
          .name(request.getName())
          .email(request.getEmail())
          .mobileNumber(request.getMobileNumber())
          .phoneNumber(request.getPhoneNumber())
          .role(request.getRole())
          .build();

      memberRepository.save(newMember);
    } catch (IllegalArgumentException e) {
      throw new PracException(PASSWORD_ENCRYPTION_FAIL);
    } catch (NoSuchAlgorithmException e) {
      throw new PracException(KEY_GENERATION_FAIL);
    } catch (Exception e) {
      throw new PracException(REGISTER_MEMBER_FAIL);
    }
  }

  public MemberInfoResponse getMemberInfo(String account) throws NotFoundException {
    Member member = memberRepository.findMemberByAccount(account)
        .orElseThrow(NotFoundException::new);

    return new MemberInfoResponse(member);
  }

  public MemberInfoAllResponse getAllMemberInfo() {
    return new MemberInfoAllResponse(memberRepository.findAll());
  }
}
