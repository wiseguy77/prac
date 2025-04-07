package wise.study.prac.dto;

import java.util.List;
import lombok.Data;
import wise.study.prac.entity.Member;

@Data
public class MemberInfoAllResponse {

  List<MemberInfoResponse> memberList;

  public MemberInfoAllResponse(List<Member> members) {

    memberList = members.stream().map(MemberInfoResponse::new).toList();
  }
}
