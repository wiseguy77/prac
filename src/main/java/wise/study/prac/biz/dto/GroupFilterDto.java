package wise.study.prac.biz.dto;

import wise.study.prac.biz.repository.criteria.field.Filter;

public interface GroupFilterDto extends FilterDto {

  Filter getGroupFilter();
}
