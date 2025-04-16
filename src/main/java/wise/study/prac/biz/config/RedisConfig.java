package wise.study.prac.biz.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisExecProvider;
import redis.embedded.RedisServer;
import redis.embedded.RedisServerBuilder;
import redis.embedded.util.OS;

@Configuration
public class RedisConfig {

  private RedisServer redisServer;
  @Value("${spring.data.redis.port}")
  private int port;

  @PostConstruct
  public void startRedis() throws IOException {

    RedisExecProvider redisExecProvider = RedisExecProvider.defaultProvider()
        .override(OS.MAC_OS_X, "/usr/local/bin/redis-server"); // M1 대응

    redisServer = new RedisServerBuilder()
        .redisExecProvider(redisExecProvider)
        .port(port)
        .setting("maxmemory 128M")
        .build();

    redisServer.start();
  }

  @PreDestroy
  public void stopRedis() {
    if (redisServer != null) {
      redisServer.stop();
    }
  }
}
