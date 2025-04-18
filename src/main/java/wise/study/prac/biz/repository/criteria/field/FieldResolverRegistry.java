package wise.study.prac.biz.repository.criteria.field;

import java.util.HashMap;
import java.util.Map;
import wise.study.prac.biz.repository.criteria.field.resolver.FieldResolver;

public class FieldResolverRegistry {

  private final Map<Class<?>, FieldResolver<?>> resolvers = new HashMap<>();

  public <T> void register(Class<T> clazz, FieldResolver<T> resolver) {
    resolvers.put(clazz, resolver);
  }

  @SuppressWarnings("unchecked")
  public <T> FieldResolver<T> getResolver(Class<T> clazz) {

    return (FieldResolver<T>) resolvers.get(clazz);
  }
}


