package wise.study.prac.biz.repository.criteria;

import static java.util.Objects.isNull;
import static wise.study.prac.biz.dto.Filter.MatchType.EQUALS;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import java.lang.reflect.Field;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import wise.study.prac.biz.dto.Filter;
import wise.study.prac.biz.dto.Filter.MatchType;
import wise.study.prac.biz.exception.ErrorCode;
import wise.study.prac.biz.exception.PracException;
import wise.study.prac.biz.repository.criteria.field.FieldResolverRegistry;
import wise.study.prac.biz.repository.criteria.field.resolver.FieldResolver;
import wise.study.prac.biz.repository.criteria.sort.SortResolverRegistry;
import wise.study.prac.biz.repository.criteria.sort.resolver.SortResolver;

public class CriteriaBuilder {

  private final FieldResolverRegistry resolverRegistry;
  private final SortResolverRegistry sortResolverRegistry;

  public CriteriaBuilder(FieldResolverRegistry resolverRegistry,
      SortResolverRegistry sortResolverRegistry) {
    this.resolverRegistry = resolverRegistry;
    this.sortResolverRegistry = sortResolverRegistry;
  }

  @SuppressWarnings("unchecked")
  public <E> BooleanExpression buildField(Class<E> entityClass, String alias, /*Path<T> path, */
      Filter<?> filter) {

    if (isNull(filter) || isNull(filter.getValue())) {
      return null;
    }

    if (isNull(filter.getField())) {
      throw new PracException(ErrorCode.ILLEGAL_ARGUMENTS, "검색 조건 필드명 누락");
    }

    Object value = filter.getValue();
    MatchType matchType = Optional.ofNullable(filter.getMatchType()).orElse(EQUALS);

    FieldResolver<Object> resolver = (FieldResolver<Object>) resolverRegistry.getResolver(
        value.getClass());

    if (resolver == null) {
      return null;
    }

    return resolver.resolve(entityClass, alias, filter);
  }

  public <E> OrderSpecifier<?> buildSort(Class<E> entityClass, String alias, Sort.Order order) {

    try {
      Field field = entityClass.getDeclaredField(order.getProperty());
      Class<?> fieldType = field.getType();

      @SuppressWarnings("unchecked")
      SortResolver<Object> resolver = (SortResolver<Object>) sortResolverRegistry.getResolver(
          fieldType);
      if (resolver == null) {
        throw new IllegalArgumentException("No resolver for type: " + fieldType.getSimpleName());
      }

      PathBuilder<?> builder = new PathBuilder<>(entityClass, alias);
      return resolver.resolve(builder, order.getProperty(), order.getDirection());
    } catch (NoSuchFieldException e) {
      throw new IllegalArgumentException("No such field: " + order.getProperty());
    }
  }
}

