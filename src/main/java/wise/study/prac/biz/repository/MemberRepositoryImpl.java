package wise.study.prac.biz.repository;

import static wise.study.prac.biz.repository.criteria.field.resolver.MemberPredicate.accountEq;
import static wise.study.prac.biz.repository.criteria.field.resolver.MemberPredicate.emailEq;
import static wise.study.prac.biz.repository.criteria.field.resolver.MemberPredicate.nameEq;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import wise.study.prac.biz.dto.FieldFilter;
import wise.study.prac.biz.dto.FilterGroupRequest;
import wise.study.prac.biz.dto.MemberFilterPagingRequest;
import wise.study.prac.biz.dto.MemberFilterRequest;
import wise.study.prac.biz.dto.MemberResponse;
import wise.study.prac.biz.entity.Member;
import wise.study.prac.biz.entity.QMember;
import wise.study.prac.biz.entity.QTeam;
import wise.study.prac.biz.repository.criteria.CriteriaBuilder;
import wise.study.prac.biz.repository.criteria.field.Filter;
import wise.study.prac.biz.repository.params.MemberRepoParam;

@Slf4j
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

  private final JPAQueryFactory jpaQuery;
  private final QMember qMember = QMember.member;
  private final CriteriaBuilder criteriaBuilder;

  @Override
  public List<Member> findMemberTeamList(MemberRepoParam repoParam) {

    QTeam team = QTeam.team;

    BooleanBuilder builder = new BooleanBuilder();

    builder.and(accountEq(repoParam.getAccount()))
        .and(nameEq(repoParam.getName()))
        .and(emailEq(repoParam.getEmail()));

    return jpaQuery.selectFrom(qMember)
        .where(builder)
        .leftJoin(qMember.team, team)
        .fetchJoin()
        .fetch();
  }

  @Override
  public List<Member> filterMemberList(MemberFilterRequest filterParam) {

    String alias = "member_filter";
    QMember fMember = new QMember(alias);
    BooleanBuilder builder = new BooleanBuilder();

    // TODO : domain 정보를 기준으로 필터를 생성할 수 있도록 구현
    for (FieldFilter<?> fieldFilter : filterParam.getFilters()) {
      if (fieldFilter.isAnd()) {
        builder.and(criteriaBuilder.buildField(Member.class, alias, fieldFilter));
      } else {
        builder.or(criteriaBuilder.buildField(Member.class, alias, fieldFilter));
      }
    }

    return jpaQuery.selectFrom(fMember)
        .where(builder)
        .fetch();
  }

  @Override
  public Page<MemberResponse> filterMemberList(FilterGroupRequest filterGroupRequest,
      Pageable pageable) {

    String alias = "member_filter";
    QMember fMember = new QMember(alias);
    BooleanBuilder builder = new BooleanBuilder();

    // TODO : domain 정보를 기준으로 필터를 생성할 수 있도록 구현
    Filter filter = filterGroupRequest.getFilter();
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

    List<MemberResponse> members = query.fetch().stream().map(MemberResponse::new).toList();

    return new PageImpl<>(members, pageable, total);
  }

  @Override
  public Page<MemberResponse> filterMemberList(MemberFilterPagingRequest filterParam,
      Pageable pageable) {

    String alias = "member_filter";
    QMember fMember = new QMember(alias);
    BooleanBuilder builder = new BooleanBuilder();
    List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

    // TODO : domain 정보를 기준으로 필터를 생성할 수 있도록 구현
    for (FieldFilter<?> fieldFilter : filterParam.getFilters()) {
      if (fieldFilter.isAnd()) {
        builder.and(criteriaBuilder.buildField(Member.class, alias, fieldFilter));
      } else {
        builder.or(criteriaBuilder.buildField(Member.class, alias, fieldFilter));
      }
    }

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

    List<MemberResponse> members = query.fetch().stream().map(MemberResponse::new).toList();

    return new PageImpl<>(members, pageable, total);
  }
}
