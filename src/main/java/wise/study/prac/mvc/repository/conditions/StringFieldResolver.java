package wise.study.prac.mvc.repository.conditions;

import static wise.study.prac.mvc.exception.ErrorCode.ILLEGAL_ARGUMENTS;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiFunction;
import wise.study.prac.mvc.dto.Filter.MatchType;
import wise.study.prac.mvc.exception.PracException;

public class StringFieldResolver implements FieldResolver<String> {

  private static final Map<MatchType, BiFunction<StringPath, String, BooleanExpression>> ops = new EnumMap<>(
      MatchType.class);

  static {
    ops.put(MatchType.EQUALS, SimpleExpression::eq);
    ops.put(MatchType.CONTAINS, StringExpression::containsIgnoreCase);
    ops.put(MatchType.STARTS_WITH, StringExpression::startsWithIgnoreCase);
    ops.put(MatchType.ENDS_WITH, StringExpression::endsWithIgnoreCase);
  }

  @Override
  public BooleanExpression resolve(Path<String> path, String value, MatchType matchType) {

    if (!(path instanceof StringPath stringPath)) {
      throw new PracException(ILLEGAL_ARGUMENTS);
    }

    return ops.getOrDefault(matchType, SimpleExpression::eq)
        .apply(stringPath, value);
  }
}


