package wise.study.prac.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wise.study.prac.dto.MemberInfoAllResponse;
import wise.study.prac.dto.MemberInfoResponse;
import wise.study.prac.dto.RegisterMemberRequest;
import wise.study.prac.entity.Member;
import wise.study.prac.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public void registerMember(RegisterMemberRequest request) {

    Member newMember = Member.builder()
        .account(request.getAccount())
        .password(passwordEncoder.encode(request.getPassword()))
        .name(request.getName())
        .email(request.getEmail())
        .mobileNumber(request.getMobileNumber())
        .phoneNumber(request.getPhoneNumber())
        .role(request.getRole())
        .build();

    memberRepository.save(newMember);
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
