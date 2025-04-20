package wise.study.prac.biz.repository.criteria.field.resolver;

import static wise.study.prac.biz.exception.ErrorCode.ILLEGAL_ARGUMENTS;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.SimpleExpression;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiFunction;
import wise.study.prac.biz.dto.FieldFilter;
import wise.study.prac.biz.dto.FieldFilter.MatchType;
import wise.study.prac.biz.exception.PracException;

public class BigDecimalFieldResolver implements FieldResolver<BigDecimal> {

  private static final Map<MatchType, BiFunction<NumberPath<BigDecimal>, BigDecimal, BooleanExpression>> ops = new EnumMap<>(
      MatchType.class);

  static {
    ops.put(MatchType.EQUALS, SimpleExpression::eq);
    ops.put(MatchType.GREATER_THAN, NumberExpression::gt);
    ops.put(MatchType.LESS_THAN, NumberExpression::lt);
    ops.put(MatchType.GREATER_OR_EQUAL, NumberExpression::goe);
    ops.put(MatchType.LESS_OR_EQUAL, NumberExpression::loe);
  }

  @Override
  public BooleanExpression resolve(Path<BigDecimal> path, BigDecimal value, MatchType matchType) {

    if (!(path instanceof NumberPath<BigDecimal> numberPath)) {
      throw new PracException(ILLEGAL_ARGUMENTS);
    }

    return ops.getOrDefault(matchType, SimpleExpression::eq)
        .apply(numberPath, value);
  }

  @Override
  public <E> BooleanExpression resolve(Class<E> entityClass, String alias,
      FieldFilter<?> fieldFilter) {

    PathBuilder<E> builder = new PathBuilder<>(entityClass, alias);
    NumberPath<BigDecimal> path = builder.getNumber(fieldFilter.getField(), BigDecimal.class);

    if (!(fieldFilter.getValue() instanceof BigDecimal value)) {
      throw new PracException(ILLEGAL_ARGUMENTS);
    }

    return ops.getOrDefault(fieldFilter.getMatchType(), NumberPath::eq).apply(path, value);
  }
}