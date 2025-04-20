package wise.study.prac.biz.dto;

import java.util.List;
import lombok.Data;
import wise.study.prac.biz.entity.Member;

@Data
public class MemberListVo {

  List<MemberVo> memberList;

  public MemberListVo(List<Member> members) {

    memberList = members.stream().map(MemberVo::new).toList();
  }
}
