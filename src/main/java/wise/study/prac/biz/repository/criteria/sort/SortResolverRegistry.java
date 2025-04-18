package wise.study.prac.biz.repository.criteria.sort;

import java.util.HashMap;
import java.util.Map;
import wise.study.prac.biz.repository.criteria.sort.resolver.SortResolver;

public class SortResolverRegistry {

  private final Map<Class<?>, SortResolver<?>> resolvers = new HashMap<>();

  public <T> void register(Class<T> clazz, SortResolver<T> resolver) {
    resolvers.put(clazz, resolver);
  }

  @SuppressWarnings("unchecked")
  public <T> SortResolver<T> getResolver(Class<T> clazz) {
    return (SortResolver<T>) resolvers.get(clazz);
  }
}
