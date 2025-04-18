package wise.study.prac.biz.repository.criteria.field.resolver;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import wise.study.prac.biz.dto.Filter;
import wise.study.prac.biz.dto.Filter.MatchType;

public interface FieldResolver<T> {

  BooleanExpression resolve(Path<T> path, T value, MatchType matchType);

  default <E> BooleanExpression resolve(Class<E> entityClass, String alias, Filter<?> filter) {
    return null;
  }
}
