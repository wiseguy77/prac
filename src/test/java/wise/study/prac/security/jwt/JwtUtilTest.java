package wise.study.prac.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.NoSuchAlgorithmException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class JwtUtilTest {

  @Test
  public void generateSecretKeyTest() throws NoSuchAlgorithmException {

    JwtUtil jwtUtil = new JwtUtil();
    String secretKey = jwtUtil.generateSecretKey();

    log.info("secretKey: {}", secretKey);

    assertThat(secretKey).isNotBlank();
  }
}