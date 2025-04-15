package wise.study.prac.mvc.repository;

import java.util.List;
import java.util.Optional;
import wise.study.prac.mvc.entity.Member;
import wise.study.prac.mvc.repository.params.MemberRepoParam;

public interface MemberRepositoryCustom {

  List<Member> findMemberOne(MemberRepoParam param);

  Optional<Member> findMemberTeam(MemberRepoParam repoParam);
}
