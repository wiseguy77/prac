package wise.study.prac.mvc.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import wise.study.prac.mvc.entity.Member;

@Data
@NoArgsConstructor
public class MemberResponse {

  private long id;
  private String name;
  private String phoneNumber;
  private String email;

  public MemberResponse(Member member) {

    this.setId(member.getId());
    this.setName(member.getName());
    this.setEmail(member.getEmail());
    this.setPhoneNumber(member.getPhoneNumber());
  }
}
