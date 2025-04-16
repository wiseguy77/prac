package wise.study.prac.mvc.dto;

import java.util.List;
import lombok.Data;
import wise.study.prac.mvc.entity.Member;

@Data
public class MemberListResponse {

  List<MemberResponse> memberList;

  public MemberListResponse(List<Member> members) {

    memberList = members.stream().map(MemberResponse::new).toList();
  }
}
