package wise.study.prac.biz.dto;

import static wise.study.prac.biz.dto.GroupFilter.LogicType.AND;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import wise.study.prac.biz.repository.criteria.field.Filter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupFilter implements Filter {

  @Builder.Default
  private String type = "group";
  private List<Filter> filters;
  @Builder.Default
  private LogicType logicType = AND;

  public enum LogicType {
    AND,
    OR;

    @JsonCreator
    public static LogicType from(String value) {
      return valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
      return this.name().toLowerCase();
    }
  }
}
