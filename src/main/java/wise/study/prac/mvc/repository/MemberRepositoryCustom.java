package wise.study.prac.mvc.repository;

import java.util.List;
import wise.study.prac.mvc.entity.Member;
import wise.study.prac.mvc.repository.params.MemberRepoFilterParam;
import wise.study.prac.mvc.repository.params.MemberRepoParam;

public interface MemberRepositoryCustom {

  List<Member> findMemberTeamList(MemberRepoParam repoParam);

  List<Member> filterMemberList(MemberRepoFilterParam filterParam);
}
