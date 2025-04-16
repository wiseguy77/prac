package wise.study.prac.mvc.repository.conditions;

import static wise.study.prac.mvc.exception.ErrorCode.ILLEGAL_ARGUMENTS;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiFunction;
import wise.study.prac.mvc.dto.Filter.MatchType;
import wise.study.prac.mvc.exception.PracException;

public class DateFieldResolver implements FieldResolver<Date> {

  private static final Map<MatchType, BiFunction<DateTimePath<Date>, Date, BooleanExpression>> ops = new EnumMap<>(
      MatchType.class);

  static {
    ops.put(MatchType.EQUALS, DateTimeExpression::eq);
    ops.put(MatchType.GREATER_THAN, DateTimeExpression::gt);
    ops.put(MatchType.LESS_THAN, DateTimeExpression::lt);
    ops.put(MatchType.GREATER_OR_EQUAL, DateTimeExpression::goe);
    ops.put(MatchType.LESS_OR_EQUAL, DateTimeExpression::loe);
  }

  @Override
  public BooleanExpression resolve(Path<Date> path, Date value, MatchType matchType) {

    if (!(path instanceof DateTimePath<Date> datePath)) {
      throw new PracException(ILLEGAL_ARGUMENTS);
    }

    return ops.getOrDefault(matchType, DateTimeExpression::eq)
        .apply(datePath, value);
  }
}