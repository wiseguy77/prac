package wise.study.prac.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import wise.study.prac.mvc.service.params.MemberSvcFilterParam;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberFilterRequest implements MemberSvcFilterParam {

  Filter<String> account;
  Filter<String> name;
  Filter<String> email;
}
