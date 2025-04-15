package wise.study.prac.mvc.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wise.study.prac.mvc.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

  Optional<Member> findById(int id);
  Optional<Member> findMemberByAccount(String account);
}
