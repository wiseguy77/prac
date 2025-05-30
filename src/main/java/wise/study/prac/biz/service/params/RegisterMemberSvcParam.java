package wise.study.prac.biz.service.params;

import wise.study.prac.security.enums.RoleType;

public interface RegisterMemberSvcParam {

  String getAccount();

  String getPassword();

  String getName();

  String getEmail();

  String getMobileNumber();

  String getPhoneNumber();

  RoleType getRole();
}
