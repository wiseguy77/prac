package wise.study.prac.biz.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import wise.study.prac.biz.repository.criteria.field.Filter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilterGroupRequest implements FilterRequest {

  Filter groupFilter;

  @Override
  public List<FieldFilter<?>> getFilters() {
    return List.of();
  }

  @Override
  public Filter getFilter() {
    return groupFilter;
  }
}
