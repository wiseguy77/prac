package wise.study.prac.biz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import wise.study.prac.biz.repository.criteria.field.Filter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchGroupDto implements GroupFilterDto {

  Filter groupFilter;

//  @Override
//  public List<Filter> getFilters() {
//    return List.of();
//  }

//  @Override
//  public Filter getGroupFilter() {
//    return groupFilter;
//  }
}
