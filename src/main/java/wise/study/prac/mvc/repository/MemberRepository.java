package wise.study.prac.mvc.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wise.study.prac.mvc.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findMemberById(Long id);
  Optional<Member> findMemberByAccount(String account);
  Optional<Member> findMemberByAccessToken(String accessToken);
  Optional<Member> findMemberByRefreshToken(String refreshToken);
}
