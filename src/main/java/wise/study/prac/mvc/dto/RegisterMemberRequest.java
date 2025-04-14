package wise.study.prac.mvc.dto;

import lombok.Data;
import wise.study.prac.mvc.service.params.RegisterMemberSvcParam;
import wise.study.prac.security.enums.RoleType;

@Data
public class RegisterMemberRequest implements RegisterMemberSvcParam {

  private String account;
  private String password;
  private String name;
  private String email;
  private String mobileNumber;
  private String phoneNumber;
  private RoleType role = RoleType.USER;
}
