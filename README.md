# 🔥 스프링 프로젝트 템플릿

이 프로젝트는 **다양한 스프링 기반 프로젝트에서 공통으로 재사용 가능한 템플릿 레퍼런스**를 만드는 것을 목표로 합니다. 실무 신규 프로젝트에서도 활용할 수 있도록 깔끔하고
완성도 있는 구조를 갖춘 **독립형 스프링 프로젝트**입니다.

# 📚 목차

1. [프로젝트 소개](#-프로젝트-소개)
2. [기술 스택](#-기술-스택)
3. [향후 예정 작업](#-향후-예정-작업)
4. [진행 중 / 완료 작업](#-진행-중--완료-작업)
    1) [응답 추상화(완료)](#1-응답-추상화완료)
    2) [로깅(완료)](#2-로깅완료)
    3) [단계별 인증(완료)](#3-단계별-인증완료)
    4) [QueryDSL 추상화(완료)](#4-QueryDSL-추상화완료)
5. [라이센스](#-라이센스)

---

# 📌 프로젝트 소개

- 다양한 스프링 기반 프로젝트에서 공통으로 재사용 가능한 **템플릿 레퍼런스** 만들기
- 외부 시스템(DB, API 등)에 의존하지 않고 동작 가능한 **독립형 스프링 프로젝트** 구축
- 새 프로젝트에 사용할 수 있는 **깔끔하고 완성도 있는 구조** 잡기

---

# 🔧 기술 스택

- ![Java](https://img.shields.io/badge/Java-17-orange?logo=java)
  ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen?logo=springboot)
  ![Spring Security](https://img.shields.io/badge/Spring%20Security-6.4.4-brightgreen?logo=springsecurity)
  ![Spring Data](https://img.shields.io/badge/Spring%20Data-3.4.4-brightgreen?logo=spring&logoColor=white)
  ![QueryDSL](https://img.shields.io/badge/QueryDSL-6.1.1-blue)
  ![H2 Database](https://img.shields.io/badge/H2%20DB-in--memory-lightgrey?logo=H2%20database)
  ![Redis](https://img.shields.io/badge/Redis-in--memory-red?logo=redis)
  ![Logback](https://img.shields.io/badge/Logback-1.5.18-pink)
  ![OpenApi](https://img.shields.io/badge/OpenApi-doc-purple?logo=openapiinitiative)
  ![Maven](https://img.shields.io/badge/Maven-build-blue?logo=apachemaven)
  ![License](https://img.shields.io/badge/license-MIT-green)
---

# 🌱 향후 예정 작업

- OAuth 2.0 로그인 인증 구현 및 국내 벤더용 OAuth 커스텀 등록 (예: 네이버, 카카오)
- 로깅 고도화 : Marker, AsyncAppender 적용
- 테스트 코드 작성 : MockMvc + 통합 테스트
- 인증 흐름 시퀀스 다이어그램 작성 및 문서화
- 다국어 지원(메시지 리소스)
- **dockerfile 이미지 생성과 docker-compose 실행환경 구성**

---

# 🌟 진행 중 / 완료 작업

## 1. 응답 추상화(완료)

<details>
<summary><span style="font-size:15px">자세히</span></summary>

- CommonResponse 응답 객체 정의
- AOP-like GlobalResponseAdvice 핸들러 구현(완료)
- AOP-like GlobalExceptionAdvice 핸들러 구현(완료)
- ErrorCode 정의: 클라이언트와 서버에러 구분 및 HttpStatus 매핑

### 📄 GlobalResponseAdvice.java

```java

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {

  private final ObjectMapper objectMapper;

  @Override
  public boolean supports(MethodParameter returnType,
      Class<? extends HttpMessageConverter<?>> converterType) {

    String path = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
        .getRequest().getRequestURI();

    return !path.startsWith("/v3/api-docs") &&
        !path.startsWith("/swagger") &&
        !path.startsWith("/swagger-ui");
  }

  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType,
      MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
      ServerHttpRequest request, ServerHttpResponse response) {

    if (body instanceof CommonResponse) {
      return body;
    }

    if (body instanceof String) {
      try {
        return objectMapper.writeValueAsString(CommonResponse.success(body));
      } catch (JsonProcessingException e) {
        throw new RuntimeException("응답 직렬화 실패", e);
      }
    }

    return CommonResponse.success(body);
  }
}
```

### 📄 GlobalExceptionAdvice.java

```java

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

  @ExceptionHandler(PracException.class)
  public ResponseEntity<CommonResponse<?>> handleCustomException(PracException e) {

    log.error("[Exception] : ", e);

    return ResponseEntity
        .status(e.getErrorCode().getHttpStatus())
        .body(CommonResponse.fail(e.getErrorCode()));
  }

  @ExceptionHandler({
      NoResourceFoundException.class, HttpMessageNotReadableException.class,
      MethodArgumentNotValidException.class})
  public ResponseEntity<CommonResponse<?>> handleGenericException(Exception e) {

    log.error("[Exception] : ", e);
    ErrorCode errorCode = ErrorCode.BAD_REQUEST;

    if (e instanceof NoResourceFoundException) {
      errorCode = ErrorCode.NO_RESOURCE_FOUND;
    } else if (e instanceof HttpMessageNotReadableException
        || e instanceof MethodArgumentNotValidException) {
      errorCode = ErrorCode.INVALID_REQUEST_BODY;
    }

    return ResponseEntity
        .status(errorCode.getHttpStatus())
        .body(CommonResponse.fail(errorCode));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<CommonResponse<?>> handleGenericException(Exception e,
      HttpServletRequest request) {

    if (request.getRequestURI().startsWith("/v3/api-docs") ||
        request.getRequestURI().startsWith("/swagger") ||
        request.getRequestURI().startsWith("/swagger-ui")) {
      return null; // Spring 기본 처리로 넘기기
    }

    log.error("[Exception] : ", e);

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(CommonResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR));
  }
}
```

</details>

## 2. 로깅(완료)

<details>
<summary><span style="font-size:15px">자세히</span></summary>

- logback을 사용합니다.
- 요청별로 로깅에 필요한 데이터를 대비해 MDC(Mapped Diagnostic Context)를 준비합니다.
- Thread Local MDC 보안을 강화하기 위해 스프링 시큐리티에 CleanUpFilter를 추가해 리셋합니다.

### 📄 CleanUpFilter.java

```java

@Slf4j
public class CleanUpFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      String clientIp = AuditUtil.getClientIp(request);
      MDC.put(REQUEST_IP.getMdcKey(), clientIp);
// (선택) 요청 전 처리
      filterChain.doFilter(request, response);
    } finally {
      SecurityContextHolder.clearContext();
      MDC.clear();
      log.info("Clean up complete.");
    }
  }
}
```

</details>

## 3. 단계별 인증(완료)

<details>
<summary><span style="font-size:15px">자세히</span></summary>

- 인증 단계별 Custom Provider와 Token 구현
- 로그인(ID/PW): 기본적인 로그인 인증 흐름 구현, 사용자는 ID와 비밀번호로 로그인 기능
- OTP(2차 인증): 두 번째 인증 단계로 OTP 방식 추가, 보안 강화를 위한 기능
- JWT 기반 인증 흐름: JWT를 활용한 인증, 토큰 생성 및 관리 기능
- Redis를 통한 RefreshToken 및 OTP 저장/관리
- JWT의 RefreshToken과 OTP를 Redis에 저장해 세션 관리 및 빠른 인증 처리 가능
- 🔐 인증 흐름 개요

```plaintext
  1. [사용자] → 로그인 요청 (ID/PW)
  2. [서버] → 사용자 인증 (Member DB 조회)
     ↳ 일치하면 OTP 생성 → Redis 저장
  3. [서버] → OTP 발송 (이메일, SMS 등)
  4. [사용자] → OTP 입력
  5. [서버] → Redis에서 OTP 검증
     ↳ 성공 시 AccessToken + RefreshToken 생성 → Redis 저장
  6. [서버] → JWT 응답 (헤더에 실어 반환)
  7. 이후 요청부터는 → JWT AccessToken 인증 필터에서 처리
```

### 1. **ID/PW 로그인 (LoginFilter, LoginAuthToken & Provider)**

- 사용자가 **ID**와 **비밀번호**를 입력하면 `LoginAuthProvider`가 이를 처리하고, 사용자 인증을 진행합니다.
- 로그인 시 `LoginAuthToken` 객체를 생성하여 `AuthenticationManager`에게 전달하고, 인증을 처리한 후 사용자를 반환합니다.
- 인증이 완료되면 OtpClientProxy가 발행해준 OTP 코드를 응답에 포함해 전달합니다.

### 2. **OTP 인증 (OtpFilter, OtpAuthToken & Provider)**

- **OTP 인증**은 기본적인 ID/PW 인증 후 추가적으로 **일회용 비밀번호(OTP)**를 입력받아 확인하는 과정입니다.
- OTP 인증을 담당하는 `OtpAuthProvider`는 `OtpAuthToken` 객체를 사용하여 OTP 코드와 DB 혹은 Redis에 저장된 OTP를 비교합니다.
- OTP 인증 후 유효성 검증이 완료되면 , **JWT 토큰**을 발급합니다.

### 3. **JWT 인증 (JwtFilter, JwtAuthToken, JwtAuthProvider)**

- **JWT 인증**은 사용자가 로그인 후 발급받은 JWT 토큰을 이용하여 인증을 진행합니다.
- 클라이언트가 서버로 요청 시 `Authorization` 헤더에 JWT를 포함해 보냅니다.
- `JwtAuthProvider`는 이 JWT 토큰의 유효성 검사를 진행하고, 토큰에 포함된 사용자 정보를 통해 `JwtAuthToken` 객체를 생성합니다.
- JWT가 유효하면, 사용자 정보를 포함한 인증 정보를 Spring Security 컨텍스트에 저장하여 인증 상태를 유지합니다.
- 보안을 위해 JWT 암호키는 사용자별로 관리합니다.

### 4. 인증 예외 처리

- `CustomAccessDeniedHandler`와 `CustomAuthenticationEntryPoint`는 **Spring Security**에서 발생하는 인증 관련
  예외를 처리하는 **핸들러**로,
  응답 상태와 메시지를 **일관성 있게** 반환하여 클라이언트가 적절한 처리 및 피드백을 받을 수 있도록 합니다.
    - **인증이 필요한 리소스 요청**
      사용자가 인증이 필요한 리소스를 요청할 때, 인증되지 않았다면 `CustomAuthenticationEntryPoint`가 호출됩니다.
        - **상태 코드**: `401 Unauthorized`
        - **응답 메시지**: `CommonResponse.fail(ErrorCode.UNAUTHORIZED)`
    - **인증은 되었으나 권한 부족**
      사용자가 권한이 없는 리소스를 요청할 때, `CustomAccessDeniedHandler`가 호출됩니다.
        - **상태 코드**: `403 Forbidden`
        - **응답 메시지**: `CommonResponse.fail(ErrorCode.ACCESS_DENIED)`
- 예외를 **커스터마이즈**하여, 세부적인 **에러 코드**와 **HTTP 상태 코드**를 동적으로 설정할 수 있어 **유연한 에러 처리**가 가능합니다.
  이를 통해 인증 및 권한 관련 예외 처리 시스템을 견고하게 만들 수 있습니다.
    - `ErrorCode.UNAUTHORIZED`: 기본적으로 사용되는 오류 코드.
    - `HttpStatus.UNAUTHORIZED.value()`: 기본적으로 반환하는 HTTP 상태 코드 (401).
    - `PracAuthenticationException`: 커스텀 예외로, 인증 실패 시 더 세부적인 정보 제공.

### 🧑‍💻 5. Code

#### 1. `CustomAccessDeniedHandler`

- **목적**
    - 사용자가 **권한이 없는** 리소스를 접근하려 할 때 발생하는 `AccessDeniedException`을 처리합니다.
- **동작**
    - 기본적으로 **403 Forbidden** 상태 코드와 `CommonResponse`를 통해 **권한 부족** 오류 메시지를 JSON 형식으로 반환합니다.
    - `AccessDeniedHandler`에서 처리할 수 있도록 `AccessDeniedException`을 상속한 `PracAccessDeniedException`을
      정의합니다.
    - `PracAccessDeniedException`이 발생하면, 커스텀 에러 코드와 HTTP 상태 코드를 **동적으로** 변경하여 처리할 수 있습니다.

### 📄 CustomAccessDeniedHandler.java

```java

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException {

    // 기본 값 설정
    ErrorCode errorCode = ErrorCode.ACCESS_DENIED;
    int status = HttpStatus.FORBIDDEN.value();

    // PracAccessDeniedException 처리
    if (accessDeniedException instanceof PracAccessDeniedException pracEx) {
      errorCode = pracEx.getErrorCode();
      status = pracEx.getHttpStatus().value();
    }

    // 응답 설정
    response.setStatus(status);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());

    // 오류 응답 작성
    CommonResponse<?> error = CommonResponse.fail(errorCode);
    response.getWriter().write(objectMapper.writeValueAsString(error));
  }
}
```

- `ErrorCode.ACCESS_DENIED`: 기본적으로 사용되는 오류 코드.
- `HttpStatus.FORBIDDEN.value()`: 기본적으로 반환하는 HTTP 상태 코드 (403).
- `PracAccessDeniedException`: 커스텀 예외로, 인증 실패 시 더 세부적인 정보 제공.
-
    - 두 핸들러 모두 **커스텀 예외**(`PracAccessDeniedException`, `PracAuthenticationException`)를 활용하여 각기 다른
      상황에 대한 세밀한 처리가 가능합니다.
- 예를 들어, JWT 토큰 만료나 잘못된 토큰을 처리할 때, `PracAuthenticationException`을 던지고,
  이를 `CustomAuthenticationEntryPoint`에서 처리하여 유저에게 적절한 메시지를 전달할 수 있습니다.

#### 2. `CustomAuthenticationEntryPoint`

- **목적**
    - 인증이 필요한 리소스를 요청했지만, **인증되지 않은 사용자**가 접근할 때 발생하는 `AuthenticationException`을 처리합니다.
- **동작**:
    - 기본적으로 **401 Unauthorized** 상태 코드를 반환하며, `CommonResponse`를 통해 **인증되지 않은 사용자** 오류 메시지를 JSON 형식으로
      반환합니다.
    - `PracAuthenticationException`이 발생하면, 커스텀 에러 코드와 HTTP 상태 코드를 **동적으로** 변경하여 처리할 수 있습니다.

### 📄 CustomAuthenticationEntryPoint.java

```java

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {

    // 기본 값 설정
    ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
    int status = HttpStatus.UNAUTHORIZED.value();

    // PracAuthenticationException 처리
    if (authException instanceof PracAuthenticationException pracEx) {
      errorCode = pracEx.getErrorCode();
      status = pracEx.getHttpStatus().value();
    }

    // 응답 설정
    response.setStatus(status);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());

    // 오류 응답 작성
    CommonResponse<?> error = CommonResponse.fail(errorCode);
    response.getWriter().write(objectMapper.writeValueAsString(error));
  }
}
```

</details>

## 4. QueryDSL 추상화(완료)

<details>
<summary> <span style="font-size:15px">자세히</span></summary>

### 🎯 목표

- **QueryDSL**을 사용하여 복잡한 쿼리와 정렬 로직을 **추상화**하고 커스텀 레포지토리로 분리해 코드의 재사용성을 높였습니다.
- `QueryDSL`을 활용한 **동적 쿼리 처리** 및 **조건 기반 필터링**을 구현해 반복적인 쿼리 코드의 중복을 없앴습니다.
- **커스텀 레포지토리**내에서 서비스 계층에서 필요한 복잡한 데이터 검색 및 처리 로직을 효율적이고 일관성 있게 처리할 수 있게 합니다.

### 🛠️ 구성

- FieldFilter
    * 주어진 필드에 대해 조건을 추가하는 필터입니다.
- GroupFilter
    * 여러 조건을 그룹화하여 조건들을 AND, OR 집합으로 다룰 수 있습니다.
- FieldResolver
    * 복잡한 조건을 해석하여 경로를 추적할 수 있게 합니다.
- FieldResolverRegistry
    * 타입별 매치 연산자를 알고 있는 리졸버들을 관리합니다.
    * 요청한 데이터에 적합한 FieldResolver 에 조건을 전달합니다.
- SortResolver
    * 정렬 조건을 처리하는 객체입니다.
- SortResolverRegistry
    * 타입별 정렬 연산자를 알고 있는 리졸버들을 관리합니다.
    * 요청한 데이터에 적합한 SortResolver 에 조건을 전달합니다.
- CriteriaBuilder
    * 쿼리와 정렬조건 querydsl 객체를 생성하는 객체입니다.
    * FieldResolverRegistry와 SortResolverRegistry에 알맞은 resolver를 탐색하고 객체 생성을 요청합니다.

### 📄 QueryDslConfig.java - 리졸버 레지스트리에 타입별 리졸버들을 등록

```java

@Configuration
public class QueryDslConfig {

  @PersistenceContext
  private EntityManager entityManager;

  @Bean
  public JPAQueryFactory jpaQueryFactory() {
    return new JPAQueryFactory(entityManager);
  }

  @Bean
  CriteriaBuilder genericPredicateBuilder() {

    FieldResolverRegistry fieldResolverRegistry = new FieldResolverRegistry();

    fieldResolverRegistry.register(String.class, new StringFieldResolver());
    fieldResolverRegistry.register(Integer.class, new IntegerFieldResolver());
    fieldResolverRegistry.register(Long.class, new LongFieldResolver());
    fieldResolverRegistry.register(BigDecimal.class, new BigDecimalFieldResolver());
    fieldResolverRegistry.register(Date.class, new DateFieldResolver());
    fieldResolverRegistry.register(LocalDateTime.class, new LocalDateTimeFieldResolver());

    SortResolverRegistry sortResolverRegistry = new SortResolverRegistry();

    sortResolverRegistry.register(String.class, new StringSortResolver());
    sortResolverRegistry.register(Integer.class, new IntegerSortResolver());
    sortResolverRegistry.register(Long.class, new LongSortResolver());
    sortResolverRegistry.register(BigDecimal.class, new BigDecimalSortResolver());
    sortResolverRegistry.register(Date.class, new DateSortResolver());
    sortResolverRegistry.register(LocalDateTime.class, new LocalDateTimeSortResolver());

    return new CriteriaBuilder(fieldResolverRegistry, sortResolverRegistry);
  }
}
```

### 📄 IntegerFieldResolver.java - 타입별 필드 리졸버 예

```java
public class IntegerFieldResolver implements FieldResolver<Integer> {

  private static final Map<MatchType, BiFunction<NumberPath<Integer>, Integer, BooleanExpression>> ops = new EnumMap<>(
      MatchType.class);

  static {
    ops.put(MatchType.EQUALS, SimpleExpression::eq);
    ops.put(MatchType.GREATER_THAN, NumberExpression::gt);
    ops.put(MatchType.LESS_THAN, NumberExpression::lt);
    ops.put(MatchType.GREATER_OR_EQUAL, NumberExpression::goe);
    ops.put(MatchType.LESS_OR_EQUAL, NumberExpression::loe);
  }

  @Override
  public BooleanExpression resolve(Path<Integer> path, Integer value, MatchType matchType) {

    if (!(path instanceof NumberPath<Integer> numberPath)) {
      throw new PracException(ILLEGAL_ARGUMENTS);
    }

    return ops.getOrDefault(matchType, SimpleExpression::eq)
        .apply(numberPath, value);
  }

  @Override
  public <E> BooleanExpression resolve(Class<E> entityClass, String alias,
      FieldFilter<?> fieldFilter) {

    PathBuilder<E> builder = new PathBuilder<>(entityClass, alias);
    NumberPath<Integer> path = builder.getNumber(fieldFilter.getField(), Integer.class);

    if (!(fieldFilter.getValue() instanceof Integer value)) {
      throw new PracException(ILLEGAL_ARGUMENTS);
    }

    return ops.getOrDefault(fieldFilter.getMatchType(), NumberPath::eq).apply(path, value);
  }
}
```

### 📄 IntegerSortResolver.java - 타입별 소트 리졸버 예

```java
public class IntegerSortResolver implements SortResolver<Integer> {

  @Override
  public OrderSpecifier<?> resolve(PathBuilder<?> builder, String fieldName,
      Sort.Direction direction) {
    NumberPath<Integer> path = builder.getNumber(fieldName, Integer.class);
    return new OrderSpecifier<>(direction.isAscending() ? Order.ASC : Order.DESC, path);
  }
}
```

---

### 📄 MemberRepositoryCustom.java - 검색 및 정렬 조건들을 일관성 있게 사용

```java
public interface MemberRepositoryCustom {

  List<Member> findMembersByPredicate(MemberSearchCondition condition);
}

public class MemberRepositoryImpl implements MemberRepositoryCustom {

  @PersistenceContext
  private EntityManager em;

  @Override
  public List<Member> filterMemberList(GroupFilterDto groupFilter) {

    String alias = "member_filter";
    QMember fMember = new QMember(alias);
    BooleanBuilder builder = new BooleanBuilder();

    // TODO : domain 정보를 기준으로 필터를 생성할 수 있도록 구현
    Filter filter = groupFilter.getGroupFilter();
    criteriaBuilder.buildGroupFilter(Member.class, alias, filter);

    return jpaQuery.selectFrom(fMember)
        .where(builder)
        .fetch();
  }

  @Override
  public Page<MemberVo> pagingMemberList(GroupFilterDto groupFilterDto, Pageable pageable) {

    String alias = "member_filter";
    QMember fMember = new QMember(alias);
    BooleanBuilder builder = new BooleanBuilder();

    // TODO : domain 정보를 기준으로 필터를 생성할 수 있도록 구현
    Filter filter = groupFilterDto.getGroupFilter();
    builder.and(criteriaBuilder.buildGroupFilter(Member.class, alias, filter));

    List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
    if (pageable.getSort().isSorted()) {
      for (Sort.Order order : pageable.getSort()) {
        orderSpecifiers.add(criteriaBuilder.buildSort(Member.class, alias, order));
      }
    }

    JPAQuery<Member> query = jpaQuery.selectFrom(fMember)
        .where(builder)
        .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize());

    long total = query.fetchCount();

    List<MemberVo> members = query.fetch().stream().map(MemberVo::new).toList();

    return new PageImpl<>(members, pageable, total);
  }
}
```

</details>

---

# 📄 라이센스

이 프로젝트는 MIT 라이센스에 따라 배포됩니다.