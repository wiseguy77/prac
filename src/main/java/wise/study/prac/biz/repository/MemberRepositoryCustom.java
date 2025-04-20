package wise.study.prac.biz.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wise.study.prac.biz.dto.GroupFilterDto;
import wise.study.prac.biz.dto.MemberDto;
import wise.study.prac.biz.dto.MemberVo;
import wise.study.prac.biz.entity.Member;

public interface MemberRepositoryCustom {

  List<Member> findMemberTeamList(MemberDto repoParam);

  List<Member> filterMemberList(GroupFilterDto groupFilterDto);

  Page<MemberVo> pagingMemberList(GroupFilterDto groupFilterDto, Pageable pageable);
}
