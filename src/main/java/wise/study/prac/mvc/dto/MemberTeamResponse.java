package wise.study.prac.mvc.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import wise.study.prac.mvc.entity.Member;

@Data
@EqualsAndHashCode(callSuper = false)
public class MemberTeamResponse extends MemberResponse {

  private long teamId;
  private String teamName;

  public MemberTeamResponse(Member member) {

    super(member);

    if (member.getTeam() != null) {
      teamId = member.getTeam().getId();
      teamName = member.getTeam().getName();
    }
  }
}
