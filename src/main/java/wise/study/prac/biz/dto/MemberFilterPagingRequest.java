package wise.study.prac.biz.dto;

import java.util.List;
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
public class MemberFilterPagingRequest implements FilterRequest {

  FieldFilter<String> account;
  FieldFilter<Integer> age;
  FieldFilter<String> name;
  FieldFilter<String> email;


  @Override
  public List<FieldFilter<?>> getFilters() {
    return Stream.of(account, age, name, email)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }
}
