package wise.study.prac.security.enums;

import lombok.Getter;

@Getter
public enum RoleType {

  ADMIN("ADMIN"), USER("USER");

  private final String authority;

  RoleType(String authority) {
    this.authority = authority;
  }
}
