package wise.study.prac.biz.repository.conditions;

import static java.util.Objects.isNull;
import static wise.study.prac.biz.dto.Filter.MatchType.EQUALS;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.Optional;
import wise.study.prac.biz.dto.Filter;
import wise.study.prac.biz.dto.Filter.MatchType;

public class GenericPredicateBuilder {

  private final FieldResolverRegistry resolverRegistry;

  public GenericPredicateBuilder(FieldResolverRegistry resolverRegistry) {
    this.resolverRegistry = resolverRegistry;
  }

  @SuppressWarnings("unchecked")
  public <T> BooleanExpression build(Path<T> path, Filter<T> filter) {

    if (isNull(filter) || isNull(filter.getValue())) {
      return null;
    }

    Object value = filter.getValue();
    MatchType matchType = Optional.ofNullable(filter.getMatchType()).orElse(EQUALS);

    FieldResolver<Object> resolver = (FieldResolver<Object>) resolverRegistry.getResolver(
        value.getClass());

    if (resolver == null) {
      return null;
    }

    return resolver.resolve((Path<Object>) path, value, matchType);
  }
}

