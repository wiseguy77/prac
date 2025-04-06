package wise.study.prac.security.form;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import wise.study.prac.enums.RoleType;
import wise.study.prac.entity.Member;

public class CustomUserDetails implements UserDetails {

  @Getter
  private long id;
  @Getter
  private String otpCode;
  @Getter
  private LocalDateTime otpExpiryTime;
  @Getter
  private RoleType role;
  private final String account;
  private String password;

  public CustomUserDetails(Member member) {
    id = member.getId();
    account = member.getAccount();
    password = member.getPassword();
    otpCode = member.getOtpCode();
    otpExpiryTime = member.getOtpExpiryTime();
    role = member.getRole();
  }

  public CustomUserDetails(String account) {
    this.account = account;
  }

  public boolean hasValidOtpCode() {

    if (otpExpiryTime == null || LocalDateTime.now().isAfter(otpExpiryTime)) {
      return false;
    }

    return otpCode != null;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return account;
  }

  @Override
  public boolean isAccountNonExpired() {
    return UserDetails.super.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return UserDetails.super.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return UserDetails.super.isCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {
    return UserDetails.super.isEnabled();
  }
}
