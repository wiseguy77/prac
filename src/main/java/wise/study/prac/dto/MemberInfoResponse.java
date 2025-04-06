package wise.study.prac.dto;

import lombok.Data;
import wise.study.prac.entity.Member;

@Data
public class MemberInfoResponse {

  public MemberInfoResponse(Member member) {

    this.setId(member.getId());
    this.setName(member.getName());
    this.setEmail(member.getEmail());
    this.setPhoneNumber(member.getPhoneNumber());
  }

  private long id;
  private String name;
  private String phoneNumber;
  private String email;
}
