package wise.study.prac.biz.dto;

import static wise.study.prac.biz.dto.FieldFilter.LogicType.AND;
import static wise.study.prac.biz.dto.FieldFilter.MatchType.EQUALS;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import wise.study.prac.biz.repository.criteria.field.Filter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldFilter<T> implements Filter {

  private String type = "field";
  private String field;
  private T value;
  private MatchType matchType = EQUALS;
  private LogicType logicType = AND;

  public enum MatchType {

    EQUALS,
    EQUALS_IGNORE_CASE,
    NOT_EQUALS,
    NOT_EQUALS_IGNORE_CASE,
    CONTAINS,
    CONTAINS_IGNORE_CASE,
    STARTS_WITH,
    ENDS_WITH,
    GREATER_THAN,
    GREATER_OR_EQUAL,
    LESS_THAN,
    LESS_OR_EQUAL;

    @JsonCreator
    public static MatchType from(String value) {
      return MatchType.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
      return this.name().toLowerCase();
    }
  }

  public enum LogicType {
    AND,
    OR;

    @JsonCreator
    public static LogicType from(String value) {
      return LogicType.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
      return this.name().toLowerCase();
    }
  }

  public boolean isAnd() {
    return logicType == AND;
  }
}
