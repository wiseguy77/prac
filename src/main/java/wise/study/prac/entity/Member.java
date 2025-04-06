package wise.study.prac.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import wise.study.prac.enums.JwtCustomClaim;
import wise.study.prac.enums.RoleType;
import wise.study.prac.util.JwtInfo;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member implements JwtInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String account;
  private String password;
  private String name;
  private String mobileNumber;
  private String phoneNumber;
  private String email;
  private String accessToken;
  private String refreshToken;
  private String secretKey;
  private String otpCode;
  private LocalDateTime otpExpiryTime;
  @Enumerated(EnumType.STRING)
  private RoleType role;

  @ManyToOne
  @JoinColumn(name = "team_id")
  private Team team;

  public void resetOtp() {
    otpCode = null;
    otpExpiryTime = null;
  }

  public void resetJwt() {
    accessToken = null;
    refreshToken = null;
  }

  @Override
  public Map<String, Object> getClaims() {
    return Map.of(
        JwtCustomClaim.ACCOUNT.name(), account,
        JwtCustomClaim.ROLE.name(), role.name()
    );
  }
}
