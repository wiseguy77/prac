package wise.study.prac.mvc.repository.conditions;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import wise.study.prac.mvc.dto.Filter.MatchType;

public interface FieldResolver<T> {

  BooleanExpression resolve(Path<T> path, T value, MatchType matchType);
}
