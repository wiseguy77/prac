package wise.study.prac.biz.dto;

import static wise.study.prac.biz.dto.FieldFilter.MatchType.EQUALS;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import wise.study.prac.biz.repository.criteria.field.Filter;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldFilter<T> implements Filter {

  @Schema(example = "group 또는 field")
  @Builder.Default
  private String type = "field";
  @Schema(example = "name", description = "필드명")
  private String field;
  @Schema(example = "홍길동")
  private T value;
  @Builder.Default
  private MatchType matchType = EQUALS;
//  private LogicType logicType = AND;

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
}
