package wise.study.prac.security.form;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import wise.study.prac.mvc.entity.Member;

@Getter
@NoArgsConstructor
public class CustomUserDetailsImpl implements CustomUserDetails {

  private long id;
  private String otpCode;
  private LocalDateTime otpExpiryTime;
  private List<SimpleGrantedAuthority> authorities;
  private String secretKey;
  private String account;
  private String password;

  public CustomUserDetailsImpl(Member member) {
    id = member.getId();
    account = member.getAccount();
    password = member.getPassword();
    otpCode = member.getOtpCode();
    otpExpiryTime = member.getOtpExpiryTime();
    authorities = new ArrayList<>(List.of(new SimpleGrantedAuthority(member.getRole().name())));
    secretKey = member.getSecretKey();
  }

  public boolean hasValidOtpCode() {

    if (otpExpiryTime == null || LocalDateTime.now().isAfter(otpExpiryTime)) {
      return false;
    }

    return otpCode != null;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
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
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
