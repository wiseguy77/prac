package wise.study.prac.biz.repository;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static wise.study.prac.biz.exception.ErrorCode.JWT_EXPIRED;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import wise.study.prac.biz.exception.PracException;
import wise.study.prac.common.util.JsonUtil;
import wise.study.prac.security.jwt.JwtIssuedInfo;
import wise.study.prac.security.jwt.JwtUserDetails;
import wise.study.prac.security.jwt.JwtUtil;

@Repository
@RequiredArgsConstructor
public class JwtRepository {

  private final RedisTemplate<String, String> redisTemplate;
  private final JwtUtil jwtUtil;
  private final JsonUtil jsonUtil;
  private final ObjectMapper objectMapper;

  public void save(JwtIssuedInfo jwtIssuedInfo) {

    String key = jwtIssuedInfo.getJwtHash();
    String value = jsonUtil.toJson(jwtIssuedInfo);
    long ttl = jwtIssuedInfo.getTtl();
    redisTemplate.opsForValue().set(key, value, ttl, MILLISECONDS);
  }

  public JwtUserDetails find(String jwt) {

    String key = jwtUtil.hash(jwt);
    String value = Optional.ofNullable(redisTemplate.opsForValue().get(key))
        .orElseThrow(() -> new PracException(JWT_EXPIRED));

    return jsonUtil.fromJson(value, JwtIssuedInfo.class);
  }

  public void delete(String jwt) {

    String key = jwtUtil.hash(jwt);
    redisTemplate.delete(key);
  }
}