package wise.study.prac.biz.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wise.study.prac.biz.repository.criteria.CriteriaBuilder;
import wise.study.prac.biz.repository.criteria.field.FieldResolverRegistry;
import wise.study.prac.biz.repository.criteria.field.resolver.BigDecimalFieldResolver;
import wise.study.prac.biz.repository.criteria.field.resolver.DateFieldResolver;
import wise.study.prac.biz.repository.criteria.field.resolver.IntegerFieldResolver;
import wise.study.prac.biz.repository.criteria.field.resolver.LocalDateTimeFieldResolver;
import wise.study.prac.biz.repository.criteria.field.resolver.LongFieldResolver;
import wise.study.prac.biz.repository.criteria.field.resolver.StringFieldResolver;
import wise.study.prac.biz.repository.criteria.sort.SortResolverRegistry;
import wise.study.prac.biz.repository.criteria.sort.resolver.BigDecimalSortResolver;
import wise.study.prac.biz.repository.criteria.sort.resolver.DateSortResolver;
import wise.study.prac.biz.repository.criteria.sort.resolver.IntegerSortResolver;
import wise.study.prac.biz.repository.criteria.sort.resolver.LocalDateTimeSortResolver;
import wise.study.prac.biz.repository.criteria.sort.resolver.LongSortResolver;
import wise.study.prac.biz.repository.criteria.sort.resolver.StringSortResolver;

@Configuration
public class QueryDslConfig {

  @PersistenceContext
  private EntityManager entityManager;

  @Bean
  public JPAQueryFactory jpaQueryFactory() {
    return new JPAQueryFactory(entityManager);
  }

  @Bean
  CriteriaBuilder genericPredicateBuilder() {

    FieldResolverRegistry fieldResolverRegistry = new FieldResolverRegistry();

    fieldResolverRegistry.register(String.class, new StringFieldResolver());
    fieldResolverRegistry.register(Integer.class, new IntegerFieldResolver());
    fieldResolverRegistry.register(Long.class, new LongFieldResolver());
    fieldResolverRegistry.register(BigDecimal.class, new BigDecimalFieldResolver());
    fieldResolverRegistry.register(Date.class, new DateFieldResolver());
    fieldResolverRegistry.register(LocalDateTime.class, new LocalDateTimeFieldResolver());

    SortResolverRegistry sortResolverRegistry = new SortResolverRegistry();

    sortResolverRegistry.register(String.class, new StringSortResolver());
    sortResolverRegistry.register(Integer.class, new IntegerSortResolver());
    sortResolverRegistry.register(Long.class, new LongSortResolver());
    sortResolverRegistry.register(BigDecimal.class, new BigDecimalSortResolver());
    sortResolverRegistry.register(Date.class, new DateSortResolver());
    sortResolverRegistry.register(LocalDateTime.class, new LocalDateTimeSortResolver());

    return new CriteriaBuilder(fieldResolverRegistry, sortResolverRegistry);
  }
}
