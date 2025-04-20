package wise.study.prac.biz.repository.criteria.field.resolver;

import static wise.study.prac.biz.exception.ErrorCode.ILLEGAL_ARGUMENTS;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateExpression;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.PathBuilder;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiFunction;
import wise.study.prac.biz.dto.FieldFilter;
import wise.study.prac.biz.dto.FieldFilter.MatchType;
import wise.study.prac.biz.exception.PracException;

public class DateFieldResolver implements FieldResolver<Date> {

  private static final Map<MatchType, BiFunction<DatePath<Date>, Date, BooleanExpression>> ops = new EnumMap<>(
      MatchType.class);

  static {
    ops.put(MatchType.EQUALS, DateExpression::eq);
    ops.put(MatchType.GREATER_THAN, DateExpression::gt);
    ops.put(MatchType.LESS_THAN, DateExpression::lt);
    ops.put(MatchType.GREATER_OR_EQUAL, DateExpression::goe);
    ops.put(MatchType.LESS_OR_EQUAL, DateExpression::loe);
  }

  @Override
  public BooleanExpression resolve(Path<Date> path, Date value, MatchType matchType) {

    if (!(path instanceof DatePath<Date> datePath)) {
      throw new PracException(ILLEGAL_ARGUMENTS);
    }

    return ops.getOrDefault(matchType, DateExpression::eq)
        .apply(datePath, value);
  }

  @Override
  public <E> BooleanExpression resolve(Class<E> entityClass, String alias,
      FieldFilter<?> fieldFilter) {

    PathBuilder<E> builder = new PathBuilder<>(entityClass, alias);
    DatePath<Date> path = builder.getDate(fieldFilter.getField(), Date.class);

    if (!(fieldFilter.getValue() instanceof Date value)) {
      throw new PracException(ILLEGAL_ARGUMENTS);
    }

    return ops.getOrDefault(fieldFilter.getMatchType(), DateExpression::gt).apply(path, value);
  }
}