package wise.study.prac.biz.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import wise.study.prac.biz.entity.Member;

@Data
@EqualsAndHashCode(callSuper = false)
public class MemberTeamVo extends MemberVo {

  private long teamId;
  private String teamName;

  public MemberTeamVo(Member member) {

    super(member);

    if (member.getTeam() != null) {
      teamId = member.getTeam().getId();
      teamName = member.getTeam().getName();
    }
  }
}
