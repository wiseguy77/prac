package wise.study.prac.security.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wise.study.prac.security.enums.JwtTokenType;
import wise.study.prac.security.enums.RoleType;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtIssuedInfo implements JwtUserDetails {

  private long id;
  private String account;
  private RoleType role;
  private String secretKey;
  private long ttl;
  private Date issuedAt;
  private Date expiresAt;
  private JwtTokenType tokenType;
  private String jwtHash;
  @JsonIgnore
  private String jwt;
}
