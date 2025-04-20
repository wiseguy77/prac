package wise.study.prac.biz.repository.criteria.field.resolver;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import wise.study.prac.biz.dto.FieldFilter;
import wise.study.prac.biz.dto.FieldFilter.MatchType;
import wise.study.prac.biz.exception.ErrorCode;
import wise.study.prac.biz.exception.PracException;

public interface FieldResolver<T> {

  BooleanExpression resolve(Path<T> path, T value, MatchType matchType);

  default <E> BooleanExpression resolve(Class<E> entityClass, String alias,
      FieldFilter<?> fieldFilter) {

    String filterType = fieldFilter.getClass().getSimpleName();
    String valueType =
        fieldFilter.getValue() != null ? fieldFilter.getValue().getClass().getSimpleName() : "null";

    throw new PracException(ErrorCode.UNSUPPORTED_OPERATION,
        String.format("[%s] (valueType: %s) 필터 리졸버를 구현하지 않았습니다.", filterType, valueType));
  }
}
