package wise.study.prac.biz.repository.criteria.field.resolver;

import static wise.study.prac.biz.exception.ErrorCode.ILLEGAL_ARGUMENTS;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.PathBuilder;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiFunction;
import wise.study.prac.biz.dto.FieldFilter;
import wise.study.prac.biz.dto.FieldFilter.MatchType;
import wise.study.prac.biz.exception.PracException;

public class LocalDateTimeFieldResolver implements FieldResolver<LocalDateTime> {

  private static final Map<MatchType, BiFunction<DateTimePath<LocalDateTime>, LocalDateTime, BooleanExpression>> ops = new EnumMap<>(
      MatchType.class);

  static {
    ops.put(MatchType.EQUALS, DateTimeExpression::eq);
    ops.put(MatchType.GREATER_THAN, DateTimeExpression::gt);
    ops.put(MatchType.LESS_THAN, DateTimeExpression::lt);
    ops.put(MatchType.GREATER_OR_EQUAL, DateTimeExpression::goe);
    ops.put(MatchType.LESS_OR_EQUAL, DateTimeExpression::loe);
  }

  @Override
  public BooleanExpression resolve(Path<LocalDateTime> path, LocalDateTime value,
      MatchType matchType) {

    if (!(path instanceof DateTimePath<LocalDateTime> datePath)) {
      throw new PracException(ILLEGAL_ARGUMENTS);
    }

    return ops.getOrDefault(matchType, DateTimeExpression::eq)
        .apply(datePath, value);
  }

  @Override
  public <E> BooleanExpression resolve(Class<E> entityClass, String alias,
      FieldFilter<?> fieldFilter) {

    PathBuilder<E> builder = new PathBuilder<>(entityClass, alias);
    DateTimePath<LocalDateTime> path = builder.getDateTime(fieldFilter.getField(),
        LocalDateTime.class);

    if (!(fieldFilter.getValue() instanceof LocalDateTime value)) {
      throw new PracException(ILLEGAL_ARGUMENTS);
    }

    return ops.getOrDefault(fieldFilter.getMatchType(), DateTimeExpression::gt).apply(path, value);
  }
}