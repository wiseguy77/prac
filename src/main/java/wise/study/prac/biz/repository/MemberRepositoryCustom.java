package wise.study.prac.biz.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wise.study.prac.biz.dto.MemberFilterPagingRequest;
import wise.study.prac.biz.dto.MemberFilterRequest;
import wise.study.prac.biz.dto.MemberResponse;
import wise.study.prac.biz.entity.Member;
import wise.study.prac.biz.repository.params.MemberRepoParam;

public interface MemberRepositoryCustom {

  List<Member> findMemberTeamList(MemberRepoParam repoParam);

  List<Member> filterMemberList(MemberFilterRequest repoParam);

  Page<MemberResponse> filterMemberList(MemberFilterPagingRequest repoParam, Pageable pageable);
}
