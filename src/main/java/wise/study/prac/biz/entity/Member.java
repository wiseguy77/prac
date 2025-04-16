package wise.study.prac.biz.entity;

import static wise.study.prac.security.enums.JwtCustomClaim.ACCOUNT;
import static wise.study.prac.security.enums.JwtCustomClaim.ID;
import static wise.study.prac.security.enums.JwtCustomClaim.ROLE;
import static wise.study.prac.security.enums.JwtCustomClaim.TOKEN_TYPE;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import wise.study.prac.biz.enums.MemberStatus;
import wise.study.prac.security.enums.JwtTokenType;
import wise.study.prac.security.enums.RoleType;
import wise.study.prac.security.jwt.JwtInfo;

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
  @Transient
  JwtTokenType jwtTokenType;
  private LocalDateTime otpExpiryTime;
  @Enumerated(EnumType.STRING)
  private RoleType role;

  @ManyToOne
  @JoinColumn(name = "team_id")
  private Team team;
  @Enumerated(EnumType.STRING)
  private MemberStatus status = MemberStatus.ACTIVE;

  public void resetOtp() {
    otpCode = null;
    otpExpiryTime = null;
  }

  public void resetJwt() {
    accessToken = null;
    refreshToken = null;
  }

  public boolean isActive() {
    return status == MemberStatus.ACTIVE;
  }

  @Override
  public Map<String, Object> getClaims() {
    Map<String, Object> claims = new HashMap<>();
    claims.put(ID.getKey(), id);
    claims.put(ACCOUNT.getKey(), account);
    claims.put(ROLE.getKey(), role);
    claims.put(TOKEN_TYPE.getKey(), "");

    return claims;
  }
}
