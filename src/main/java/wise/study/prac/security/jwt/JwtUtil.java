package wise.study.prac.security.jwt;

import static wise.study.prac.security.enums.JwtCustomClaim.ACCOUNT;
import static wise.study.prac.security.enums.JwtCustomClaim.ID;
import static wise.study.prac.security.enums.JwtCustomClaim.ROLE;
import static wise.study.prac.security.enums.JwtCustomClaim.TOKEN_TYPE;
import static wise.study.prac.security.enums.JwtHeader.getHeaders;
import static wise.study.prac.security.enums.JwtRegClaim.EXP;
import static wise.study.prac.security.enums.JwtRegClaim.IAT;
import static wise.study.prac.security.enums.JwtRegClaim.getRegClaims;
import static wise.study.prac.security.enums.JwtTokenType.ACCESS;
import static wise.study.prac.security.enums.JwtTokenType.REFRESH;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;
import wise.study.prac.security.enums.JwtTokenType;
import wise.study.prac.security.enums.RoleType;

@Component
@RequiredArgsConstructor
public class JwtUtil {

  public Claims validate(String token, String secretKey) {
    SecretKey signKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

    Jws<Claims> jws = Jwts.parser().verifyWith(signKey).build().parseSignedClaims(token);

    return jws.getPayload();
  }

  public JwtIssuedInfo issueAccessJwt(JwtInfo jwtInfo, long ttl) {
    return issueJwt(jwtInfo, ACCESS, ttl);
  }

  public JwtIssuedInfo issueRefreshJwt(JwtInfo jwtInfo, long ttl) {
    return issueJwt(jwtInfo, REFRESH, ttl);
  }

  private JwtIssuedInfo issueJwt(JwtInfo jwtInfo, JwtTokenType tokenType, long ttl) {

    Date issuedAt = new Date();
    Date expiresAt = new Date(issuedAt.getTime() + ttl);

    Map<String, Object> claims = new HashMap<>();
    claims.putAll(getHeaders());
    claims.putAll(getRegClaims());
    claims.put(IAT.getKey(), issuedAt.getTime());
    claims.put(EXP.getKey(), expiresAt.getTime());
    claims.putAll(jwtInfo.getClaims());
    claims.put(TOKEN_TYPE.getKey(), tokenType.getValue());

    String secretKey = jwtInfo.getSecretKey();
    SecretKey signKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    String jwt = Jwts.builder().claims(claims).signWith(signKey).compact();

    return JwtIssuedInfo.builder()
        .id((long) claims.get(ID.getKey()))
        .account((String) claims.get(ACCOUNT.getKey()))
        .role((RoleType) claims.get(ROLE.getKey()))
        .jwt(jwt)
        .jwtHash(hash(jwt))
        .ttl(ttl)
        .secretKey(secretKey)
        .issuedAt(issuedAt)
        .expiresAt(expiresAt)
        .tokenType(tokenType).build();
  }

  public String hash(String jwt) {

    Optional.ofNullable(jwt).orElseThrow(InvalidParameterException::new);

    return DigestUtils.sha256Hex(jwt);
  }

  public String generateSecretKey() throws NoSuchAlgorithmException {

    KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
    keyGen.init(256); // 256비트
    SecretKey key = keyGen.generateKey();

    return Base64.getEncoder().encodeToString(key.getEncoded());
  }
}
