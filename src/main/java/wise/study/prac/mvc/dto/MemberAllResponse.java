package wise.study.prac.mvc.dto;

import java.util.List;
import lombok.Data;
import wise.study.prac.mvc.entity.Member;

@Data
public class MemberAllResponse {

  List<MemberResponse> memberList;

  public MemberAllResponse(List<Member> members) {

    memberList = members.stream().map(MemberResponse::new).toList();
  }
}
