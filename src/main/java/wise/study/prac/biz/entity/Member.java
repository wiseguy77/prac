package wise.study.prac.biz.entity;

import static wise.study.prac.security.enums.JwtCustomClaim.ACCOUNT;
import static wise.study.prac.security.enums.JwtCustomClaim.ID;
import static wise.study.prac.security.enums.JwtCustomClaim.ROLE;
import static wise.study.prac.security.enums.JwtCustomClaim.TOKEN_TYPE;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class Member extends BaseEntity implements JwtInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(unique = true, nullable = false)
  private String account;
  @Column(nullable = false)
  private String password;
  @Column(nullable = false)
  private String name;
  private Integer age;
  @Column(nullable = false)
  private String mobileNumber;
  private String phoneNumber;
  @Column(nullable = false)
  private String email;
  @Column(nullable = false)
  private String secretKey;
  private String otpCode;
  @Transient
  JwtTokenType jwtTokenType;
  private LocalDateTime otpExpiryTime;
  @Builder.Default
  @Enumerated(EnumType.STRING)
  private RoleType role = RoleType.USER;

  @ManyToOne
  @JoinColumn(name = "team_id")
  private Team team;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  private MemberStatus status = MemberStatus.ACTIVE;

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

  public void activate() {
    this.status = MemberStatus.ACTIVE;
  }

  public void inactivate() {
    this.status = MemberStatus.INACTIVE;
  }

  public void suspend() {
    this.status = MemberStatus.SUSPENDED;
  }

  public void block() {
    this.status = MemberStatus.BLOCKED;
  }

  public void withdraw() {
    this.status = MemberStatus.WITHDRAWN;
  }

  public void delete() {
    this.status = MemberStatus.DELETED;
  }

  public void dormant() {
    this.status = MemberStatus.DORMANT;
  }
}
