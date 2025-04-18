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

  Filter<String> account;
  Filter<Integer> age;
  Filter<String> name;
  Filter<String> email;


  @Override
  public List<Filter<?>> getFilters() {
    return Stream.of(account, age, name, email)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }
}
