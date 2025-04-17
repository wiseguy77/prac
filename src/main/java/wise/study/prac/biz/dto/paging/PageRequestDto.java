package wise.study.prac.biz.dto.paging;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import wise.study.prac.biz.dto.sort.SortCondition;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRequestDto {

  List<SortCondition> sortConditions = new ArrayList<>();
  private int page; // 0 base
  private int size;

  public Pageable toPageable() {
    List<Sort.Order> orders = sortConditions.stream()
        .map(sc -> new Sort.Order(
            Sort.Direction.fromString(sc.getDirection().toUpperCase()),
            sc.getField()))
        .collect(Collectors.toList());

    return PageRequest.of(page, size, Sort.by(orders));
  }
}
