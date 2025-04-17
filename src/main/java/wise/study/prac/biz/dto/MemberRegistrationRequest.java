package wise.study.prac.biz.dto;

import lombok.Data;
import wise.study.prac.biz.service.params.RegisterMemberSvcParam;
import wise.study.prac.security.enums.RoleType;

@Data
public class MemberRegistrationRequest implements RegisterMemberSvcParam {

  private String account;
  private String password;
  private String name;
  private String email;
  private String mobileNumber;
  private String phoneNumber;
  private RoleType role = RoleType.USER;
}
