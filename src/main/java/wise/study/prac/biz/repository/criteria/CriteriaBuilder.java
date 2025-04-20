package wise.study.prac.biz.repository.criteria;

import static java.util.Objects.isNull;
import static wise.study.prac.biz.dto.GroupFilter.LogicType.AND;
import static wise.study.prac.biz.exception.ErrorCode.ILLEGAL_ARGUMENTS;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import java.lang.reflect.Field;
import org.springframework.data.domain.Sort;
import wise.study.prac.biz.dto.FieldFilter;
import wise.study.prac.biz.dto.GroupFilter;
import wise.study.prac.biz.exception.PracException;
import wise.study.prac.biz.repository.criteria.field.FieldResolverRegistry;
import wise.study.prac.biz.repository.criteria.field.Filter;
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

  public <E> BooleanExpression buildGroupFilter(Class<E> entityClass, String alias, Filter filter) {

    BooleanExpression result = null;

    if (filter instanceof GroupFilter groupFilter) {
      for (Filter childFilter : groupFilter.getFilters()) {

        BooleanExpression builtFilter = buildGroupFilter(entityClass, alias, childFilter);

        if (builtFilter == null) {
          continue;
        }

        if (result == null) {
          result = builtFilter;
        } else {
          result = groupFilter.getLogicType() == AND ?
              result.and(builtFilter) : result.or(builtFilter);
        }
      }
      return result;
    } else if (filter instanceof FieldFilter<?> fieldFilter) {
      return buildField(entityClass, alias, fieldFilter);
    } else {
      throw new PracException(ILLEGAL_ARGUMENTS, "잘못된 형식의 검색 조건입니다.");
    }

//    return result;
  }


  @SuppressWarnings("unchecked")
  public <E> BooleanExpression buildField(Class<E> entityClass, String alias, /*Path<T> path, */
      FieldFilter<?> fieldFilter) {

    if (isNull(fieldFilter) || isNull(fieldFilter.getValue())) {
      return null;
    }

    if (isNull(fieldFilter.getField())) {
      throw new PracException(ILLEGAL_ARGUMENTS, "검색 조건 필드명 누락");
    }

    Object value = fieldFilter.getValue();
//    MatchType matchType = Optional.ofNullable(fieldFilter.getMatchType()).orElse(EQUALS);

    FieldResolver<Object> resolver = (FieldResolver<Object>) resolverRegistry.getResolver(
        value.getClass());

    if (resolver == null) {
      return null;
    }

    return resolver.resolve(entityClass, alias, fieldFilter);
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

