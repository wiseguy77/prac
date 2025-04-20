package wise.study.prac.biz.dto;

import java.util.List;
import wise.study.prac.biz.repository.criteria.field.Filter;

public interface FilterRequest {

  List<FieldFilter<?>> getFilters();

  default Filter getFilter() {
    return null;
  }
}
