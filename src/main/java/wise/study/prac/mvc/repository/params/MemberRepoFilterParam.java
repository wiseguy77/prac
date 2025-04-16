package wise.study.prac.mvc.repository.params;

import wise.study.prac.mvc.dto.Filter;

public interface MemberRepoFilterParam {

  Filter<String> getAccount();

  Filter<String> getName();

  Filter<String> getEmail();
}
