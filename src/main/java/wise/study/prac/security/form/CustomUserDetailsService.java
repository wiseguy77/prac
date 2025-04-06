package wise.study.prac.security.form;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import wise.study.prac.repository.MemberRepository;
import wise.study.prac.entity.Member;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Member member = memberRepository.findMemberByAccount(username)
        .orElseThrow(() -> new UsernameNotFoundException("로그인 실패"));

    return new CustomUserDetails(member);
  }
}
