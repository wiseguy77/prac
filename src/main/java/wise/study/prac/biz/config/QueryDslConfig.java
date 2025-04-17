package wise.study.prac.biz.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wise.study.prac.biz.repository.conditions.BigDecimalFieldResolver;
import wise.study.prac.biz.repository.conditions.DateFieldResolver;
import wise.study.prac.biz.repository.conditions.FieldResolverRegistry;
import wise.study.prac.biz.repository.conditions.GenericPredicateBuilder;
import wise.study.prac.biz.repository.conditions.IntegerFieldResolver;
import wise.study.prac.biz.repository.conditions.LocalDateTimeFieldResolver;
import wise.study.prac.biz.repository.conditions.LongFieldResolver;
import wise.study.prac.biz.repository.conditions.StringFieldResolver;

@Configuration
public class QueryDslConfig {

  @PersistenceContext
  private EntityManager entityManager;

  @Bean
  public JPAQueryFactory jpaQueryFactory() {
    return new JPAQueryFactory(entityManager);
  }

  @Bean
  GenericPredicateBuilder genericPredicateBuilder() {

    FieldResolverRegistry registry = new FieldResolverRegistry();

    registry.register(String.class, new StringFieldResolver());
    registry.register(Integer.class, new IntegerFieldResolver());
    registry.register(Long.class, new LongFieldResolver());
    registry.register(BigDecimal.class, new BigDecimalFieldResolver());
    registry.register(Date.class, new DateFieldResolver());
    registry.register(LocalDateTime.class, new LocalDateTimeFieldResolver());

    return new GenericPredicateBuilder(registry);
  }
}
