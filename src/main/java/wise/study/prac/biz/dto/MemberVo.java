package wise.study.prac.biz.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import wise.study.prac.biz.entity.Member;

@Data
@NoArgsConstructor
public class MemberVo {

  private long id;
  private String name;
  private int age;
  private String phoneNumber;
  private String email;

  public MemberVo(Member member) {

    this.setId(member.getId());
    this.setName(member.getName());
    this.setAge(member.getAge());
    this.setEmail(member.getEmail());
    this.setPhoneNumber(member.getPhoneNumber());
  }
}
