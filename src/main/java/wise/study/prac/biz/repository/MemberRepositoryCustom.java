package wise.study.prac.biz.repository;

import java.util.List;
import wise.study.prac.biz.entity.Member;
import wise.study.prac.biz.repository.params.MemberRepoFilterParam;
import wise.study.prac.biz.repository.params.MemberRepoParam;

public interface MemberRepositoryCustom {

  List<Member> findMemberTeamList(MemberRepoParam repoParam);

  List<Member> filterMemberList(MemberRepoFilterParam filterParam);
}
