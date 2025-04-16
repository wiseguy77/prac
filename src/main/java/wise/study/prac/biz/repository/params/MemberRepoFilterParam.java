package wise.study.prac.biz.repository.params;

import wise.study.prac.biz.dto.Filter;

public interface MemberRepoFilterParam {

  Filter<String> getAccount();

  Filter<String> getName();

  Filter<String> getEmail();
}
