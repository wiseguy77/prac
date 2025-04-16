package wise.study.prac.mvc.repository.conditions;

import static wise.study.prac.mvc.exception.ErrorCode.ILLEGAL_ARGUMENTS;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.SimpleExpression;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiFunction;
import wise.study.prac.mvc.dto.Filter.MatchType;
import wise.study.prac.mvc.exception.PracException;

public class IntegerFieldResolver implements FieldResolver<Integer> {

  private static final Map<MatchType, BiFunction<NumberPath<Integer>, Integer, BooleanExpression>> ops = new EnumMap<>(
      MatchType.class);

  static {
    ops.put(MatchType.EQUALS, SimpleExpression::eq);
    ops.put(MatchType.GREATER_THAN, NumberExpression::gt);
    ops.put(MatchType.LESS_THAN, NumberExpression::lt);
    ops.put(MatchType.GREATER_OR_EQUAL, NumberExpression::goe);
    ops.put(MatchType.LESS_OR_EQUAL, NumberExpression::loe);
  }

  @Override
  public BooleanExpression resolve(Path<Integer> path, Integer value, MatchType matchType) {

    if (!(path instanceof NumberPath<Integer> numberPath)) {
      throw new PracException(ILLEGAL_ARGUMENTS);
    }

    return ops.getOrDefault(matchType, SimpleExpression::eq)
        .apply(numberPath, value);
  }
}