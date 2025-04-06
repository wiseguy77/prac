package wise.study.prac.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import wise.study.prac.enums.JwtHeader;
import wise.study.prac.enums.JwtRegClaim;

@Component
@RequiredArgsConstructor
public class JwtUtil {

  private final long accessExpiration = Duration.ofMinutes(10).toMillis();
  private final long refreshExpiration = Duration.ofDays(7).toMillis();

  private final Map<String, Object> headers = JwtHeader.getHeaders();


  public String createAccessToken(JwtInfo customClaims) {

    long expiration = new Date(new Date().getTime() + accessExpiration).getTime();
    return createToken(customClaims, expiration);
  }

  public String createRefreshToken(JwtInfo customClaims) {

    long expiration = new Date(new Date().getTime() + refreshExpiration).getTime();
    return createToken(customClaims, expiration);
  }

  public Claims validateToken(String token, String secretKey) {
    SecretKey signKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

    Jws<Claims> jws = Jwts.parser().verifyWith(signKey).build().parseSignedClaims(token);

    return jws.getPayload();
  }

  private String createToken(JwtInfo customClaims, long expiration) {
    Map<String, Object> regClaims = JwtRegClaim.getRegClaims();
    regClaims.put(JwtRegClaim.EXP.name(), expiration);

    String secretKey = customClaims.getSecretKey();
    SecretKey signKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

    headers.putAll(regClaims);
    headers.putAll(customClaims.getClaims());

    return Jwts.builder().claims(headers)
        .signWith(signKey)
        .compact();
  }
}
