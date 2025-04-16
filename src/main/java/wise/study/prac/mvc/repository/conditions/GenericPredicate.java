package wise.study.prac.mvc.repository.conditions;

import static wise.study.prac.mvc.dto.Filter.MatchType.EQUALS;
import static wise.study.prac.mvc.dto.Filter.MatchType.GREATER_OR_EQUAL;
import static wise.study.prac.mvc.dto.Filter.MatchType.GREATER_THAN;
import static wise.study.prac.mvc.dto.Filter.MatchType.LESS_OR_EQUAL;
import static wise.study.prac.mvc.dto.Filter.MatchType.LESS_THAN;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringPath;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiFunction;
import wise.study.prac.mvc.dto.Filter;
import wise.study.prac.mvc.dto.Filter.MatchType;

public class GenericPredicate {


  private static final Map<MatchType, BiFunction<StringPath, String, BooleanExpression>> stringOperators;
  private static final Map<MatchType, BiFunction<NumberPath<? extends Number>, Number, BooleanExpression>> numberOperators;

  static {
    stringOperators = new EnumMap<>(MatchType.class);

    stringOperators.put(MatchType.CONTAINS, StringPath::containsIgnoreCase);
    stringOperators.put(MatchType.STARTS_WITH, StringPath::startsWithIgnoreCase);
    stringOperators.put(MatchType.ENDS_WITH, StringPath::endsWithIgnoreCase);
    stringOperators.put(EQUALS, StringPath::eq);

    numberOperators = new EnumMap<>(MatchType.class);

    numberOperators.put(GREATER_THAN, (field, value) -> {
      if (value instanceof Integer) {
        return field.gt((Integer) value);
      } else if (value instanceof Long) {
        return field.gt((Long) value);
      } else if (value instanceof BigDecimal) {
        return field.gt((BigDecimal) value);
      }
      return null;
    });

    numberOperators.put(GREATER_OR_EQUAL, (field, value) -> {
      if (value instanceof Integer) {
        return field.goe((Integer) value);
      } else if (value instanceof Long) {
        return field.goe((Long) value);
      } else if (value instanceof BigDecimal) {
        return field.goe((BigDecimal) value);
      }
      return null;
    });

    numberOperators.put(LESS_THAN, (field, value) -> {
      if (value instanceof Integer) {
        return field.lt((Integer) value);
      } else if (value instanceof Long) {
        return field.lt((Long) value);
      } else if (value instanceof BigDecimal) {
        return field.lt((BigDecimal) value);
      }
      return null;
    });

    numberOperators.put(LESS_OR_EQUAL, (field, value) -> {
      if (value instanceof Integer) {
        return field.loe((Integer) value);
      } else if (value instanceof Long) {
        return field.loe((Long) value);
      } else if (value instanceof BigDecimal) {
        return field.loe((BigDecimal) value);
      }
      return null;
    });

    numberOperators.put(EQUALS, (field, value) -> {
      if (value instanceof Integer) {
        return ((NumberPath<Integer>) field).eq((Integer) value);
      } else if (value instanceof Long) {
        return ((NumberPath<Long>) field).eq((Long) value);
      } else if (value instanceof BigDecimal) {
        return ((NumberPath<BigDecimal>) field).eq((BigDecimal) value);
      }
      return null;
    });
  }

  public static BooleanExpression apply(StringPath field, Filter<String> filter) {
    if (filter == null || filter.getValue() == null) {
      return null;
    }

    return stringOperators
        .getOrDefault(filter.getMatchType(), SimpleExpression::eq) // default: eq
        .apply(field, filter.getValue());
  }

  public static BooleanExpression apply(NumberPath<? extends Number> field, Filter<Number> filter) {
    if (filter == null || filter.getValue() == null) {
      return null;
    }

    return numberOperators.getOrDefault(filter.getMatchType(), (f, value) -> {
          if (value instanceof Integer) {
            return ((NumberPath<Integer>) f).eq((Integer) value);
          } else if (value instanceof Long) {
            return ((NumberPath<Long>) f).eq((Long) value);
          } else if (value instanceof BigDecimal) {
            return ((NumberPath<BigDecimal>) f).eq((BigDecimal) value);
          }
          return null;
        })
        .apply(field, filter.getValue());
  }
}
