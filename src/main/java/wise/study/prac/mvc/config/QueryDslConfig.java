package wise.study.prac.mvc.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wise.study.prac.mvc.repository.conditions.BigDecimalFieldResolver;
import wise.study.prac.mvc.repository.conditions.FieldResolverRegistry;
import wise.study.prac.mvc.repository.conditions.GenericPredicateBuilder;
import wise.study.prac.mvc.repository.conditions.IntegerFieldResolver;
import wise.study.prac.mvc.repository.conditions.LongFieldResolver;
import wise.study.prac.mvc.repository.conditions.StringFieldResolver;

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

    return new GenericPredicateBuilder(registry);
  }
}
