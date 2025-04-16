package wise.study.prac.mvc.dto;

import static wise.study.prac.mvc.dto.Filter.MatchType.EQUALS;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filter<T> {

  private T value;
  private MatchType matchType = EQUALS;

  public enum MatchType {

    EQUALS,
    EQUALS_IGNORE_CASE,
    NOT_EQUALS,
    NOT_EQUALS_IGNORE_CASE,
    CONTAINS,
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
}
