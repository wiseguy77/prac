package wise.study.prac.biz.dto;

import static wise.study.prac.biz.dto.GroupFilter.LogicType.OR;

import wise.study.prac.biz.dto.GroupFilter.LogicType;

public interface SimpleOrFilterDto extends FilterDto {

  default LogicType getLogicType() {
    return OR;
  }

  ;

  GroupFilterDto toGroupFilterDto();
}
