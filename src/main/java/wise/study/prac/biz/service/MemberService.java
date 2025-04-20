package wise.study.prac.biz.service;

import static wise.study.prac.biz.exception.ErrorCode.KEY_GENERATION_FAIL;
import static wise.study.prac.biz.exception.ErrorCode.PASSWORD_ENCRYPTION_FAIL;
import static wise.study.prac.biz.exception.ErrorCode.REGISTER_MEMBER_FAIL;
import static wise.study.prac.biz.exception.ErrorCode.USER_NOT_FOUND;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wise.study.prac.biz.dto.MemberDto;
import wise.study.prac.biz.dto.MemberFilterDto;
import wise.study.prac.biz.dto.MemberListVo;
import wise.study.prac.biz.dto.MemberTeamVo;
import wise.study.prac.biz.dto.MemberVo;
import wise.study.prac.biz.dto.SearchGroupDto;
import wise.study.prac.biz.dto.paging.PageResponse;
import wise.study.prac.biz.entity.Member;
import wise.study.prac.biz.exception.PracException;
import wise.study.prac.biz.repository.MemberRepository;
import wise.study.prac.biz.service.params.MemberSvcParam;
import wise.study.prac.biz.service.params.RegisterMemberSvcParam;
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

  public MemberVo getMemberById(long id) {

    Member member = memberRepository.findById(id)
        .orElseThrow(() -> new PracException(USER_NOT_FOUND));

    return new MemberVo(member);
  }

  public MemberTeamVo getMemberTeamById(long id) {

    Member member = memberRepository.findById(id)
        .orElseThrow(() -> new PracException(USER_NOT_FOUND));

    return new MemberTeamVo(member);
  }

  public MemberVo getMemberInfo(MemberSvcParam param) {
    Member member = memberRepository.findMemberByAccount(param.getAccount())
        .orElseThrow(() -> new PracException(USER_NOT_FOUND));

    return new MemberVo(member);
  }

  public MemberListVo filterMemberList(MemberFilterDto param) {

    List<Member> members = memberRepository.filterMemberList(param.toGroupFilterDto());

    return new MemberListVo(members);
  }

  public PageResponse filterMemberList(MemberFilterDto param, Pageable pageable) {

    Page<MemberVo> pageMember = memberRepository.pagingMemberList(param.toGroupFilterDto(),
        pageable);

    return new PageResponse(pageMember);
  }

  public PageResponse filterMemberList(SearchGroupDto param, Pageable pageable) {

    Page<MemberVo> pageMember = memberRepository.pagingMemberList(param, pageable);

    return new PageResponse(pageMember);
  }


  public List<MemberTeamVo> getMemberTeamList(MemberDto memberDto) {

    List<Member> members = memberRepository.findMemberTeamList(memberDto);

    return members.stream().map(MemberTeamVo::new).toList();
  }

  public MemberListVo getAllMemberInfo() {
    return new MemberListVo(memberRepository.findAll());
  }
}
