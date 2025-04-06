package wise.study.prac.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import wise.study.prac.entity.Member;

@Data
public class MemberInfoAllResponse {

  List<MemberInfoResponse> memberInfoAll = new ArrayList<>();

  public MemberInfoAllResponse(List<Member> members) {

    memberInfoAll = members.stream().map(MemberInfoResponse::new).toList();
  }
}
