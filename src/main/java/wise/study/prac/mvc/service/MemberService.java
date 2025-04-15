package wise.study.prac.mvc.service;

import static wise.study.prac.mvc.exception.ErrorCode.KEY_GENERATION_FAIL;
import static wise.study.prac.mvc.exception.ErrorCode.PASSWORD_ENCRYPTION_FAIL;
import static wise.study.prac.mvc.exception.ErrorCode.REGISTER_MEMBER_FAIL;
import static wise.study.prac.mvc.exception.ErrorCode.USER_NOT_FOUND;

import java.security.NoSuchAlgorithmException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wise.study.prac.mvc.dto.MemberAllResponse;
import wise.study.prac.mvc.dto.MemberResponse;
import wise.study.prac.mvc.dto.MemberTeamResponse;
import wise.study.prac.mvc.entity.Member;
import wise.study.prac.mvc.exception.PracException;
import wise.study.prac.mvc.repository.MemberRepository;
import wise.study.prac.mvc.service.params.MemberSvcParam;
import wise.study.prac.mvc.service.params.RegisterMemberSvcParam;
import wise.study.prac.security.jwt.JwtUtil;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  @Transactional
  public void registerMember(RegisterMemberSvcParam svcParam) {

    try {
      Member newMember = Member.builder()
          .account(svcParam.getAccount())
          .password(passwordEncoder.encode(svcParam.getPassword()))
          .secretKey(jwtUtil.generateSecretKey())
          .name(svcParam.getName())
          .email(svcParam.getEmail())
          .mobileNumber(svcParam.getMobileNumber())
          .phoneNumber(svcParam.getPhoneNumber())
          .role(svcParam.getRole())
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

  public MemberResponse getMemberById(int id) {

    Member member = memberRepository.findById(id)
        .orElseThrow(() -> new PracException(USER_NOT_FOUND));

    return new MemberResponse(member);
  }

  public MemberResponse getMemberInfo(MemberSvcParam param) {
    Member member = memberRepository.findMemberByAccount(param.getAccount())
        .orElseThrow(() -> new PracException(USER_NOT_FOUND));

    return new MemberResponse(member);
  }

  public MemberTeamResponse getMemberTeamInfo(MemberSvcParam svcParam) {

    Member member = memberRepository.findMemberTeam(svcParam)
        .orElseThrow(() -> new PracException(USER_NOT_FOUND));

    return new MemberTeamResponse(member);
  }

  public MemberAllResponse getAllMemberInfo() {
    return new MemberAllResponse(memberRepository.findAll());
  }
}
