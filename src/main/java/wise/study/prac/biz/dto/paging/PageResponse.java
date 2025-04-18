package wise.study.prac.biz.dto.paging;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse {

  boolean first;
  boolean last;
  private long total;
  private int page;
  private int size;
  private List<?> contents;

  public PageResponse(Page<?> page) {

    this.total = page.getTotalElements();
    this.page = page.getNumber();
    this.size = page.getSize();
    this.first = page.isFirst();
    this.last = page.isLast();
    this.contents = page.getContent();
  }
}
