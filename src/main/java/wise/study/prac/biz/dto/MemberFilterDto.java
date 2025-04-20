package wise.study.prac.biz.dto;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberFilterDto implements SimpleAndFilterDto {

  FieldFilter<String> account;
  FieldFilter<String> name;
  FieldFilter<String> email;

  @Override
  public GroupFilterDto toGroupFilterDto() {

    GroupFilter groupFilter = new GroupFilter();

    groupFilter.setFilters(Stream.of(account, name, email)
        .filter(Objects::nonNull)
        .collect(Collectors.toList()));

    groupFilter.setLogicType(this.getLogicType());

    return new SearchGroupDto(groupFilter);
  }
}
