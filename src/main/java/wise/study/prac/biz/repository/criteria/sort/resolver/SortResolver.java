package wise.study.prac.biz.repository.criteria.sort.resolver;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.data.domain.Sort;

public interface SortResolver<T> {

  OrderSpecifier<?> resolve(PathBuilder<?> builder, String fieldName, Sort.Direction direction);
}
