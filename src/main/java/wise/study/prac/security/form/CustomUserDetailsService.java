package wise.study.prac.security.form;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import wise.study.prac.biz.entity.Member;
import wise.study.prac.biz.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Member member = memberRepository.findMemberByAccount(username)
        .orElseThrow(() -> new UsernameNotFoundException("로그인 실패"));

    return new CustomUserDetailsImpl(member);
  }
}
