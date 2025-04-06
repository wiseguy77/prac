package wise.study.prac.dto;

import lombok.Data;
import wise.study.prac.enums.RoleType;

@Data
public class RegisterMemberRequest {

  private String account;
  private String password;
  private String name;
  private String email;
  private String mobileNumber;
  private String phoneNumber;
  private RoleType role = RoleType.USER;
}
