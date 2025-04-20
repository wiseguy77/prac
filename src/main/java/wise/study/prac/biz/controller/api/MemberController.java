package wise.study.prac.biz.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wise.study.prac.biz.dto.MemberDto;
import wise.study.prac.biz.dto.MemberFilterDto;
import wise.study.prac.biz.dto.MemberListVo;
import wise.study.prac.biz.dto.MemberRegisterDto;
import wise.study.prac.biz.dto.MemberTeamVo;
import wise.study.prac.biz.dto.MemberVo;
import wise.study.prac.biz.dto.SearchGroupDto;
import wise.study.prac.biz.dto.paging.PageResponse;
import wise.study.prac.biz.service.MemberService;
import wise.study.prac.biz.service.params.MemberSvcParam;
import wise.study.prac.security.jwt.JwtUserDetails;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  @Operation(summary = "회원 등록", description = "새로운 회원을 등록합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "회원 등록 성공", content = @Content(mediaType = "application/json", schema = @Schema())),
      @ApiResponse(responseCode = "500", description = "회원 등록 실패", content = @Content(mediaType = "application/json", schema = @Schema()))
  })
  @PostMapping()
  public void register(@RequestBody MemberRegisterDto request) {

    memberService.registerMember(request);
  }

  @Operation(summary = "로그인 사용자 정보 조회", description = "현재 로그인 중인 사용자의 정보를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "로그인 사용자 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberVo.class))),
      @ApiResponse(responseCode = "500", description = "로그인 사용자 조회 실패", content = @Content(mediaType = "application/json", schema = @Schema()))
  })
  @GetMapping("/me")
  public MemberVo getMe(@AuthenticationPrincipal JwtUserDetails user) {

    MemberSvcParam param = MemberDto.builder().account(user.getAccount()).build();
    return memberService.getMemberInfo(param);
  }

  @Operation(summary = "로그인 사용자와 팀 정보 조회", description = "현재 로그인 중인 사용자와 팀 정보를 함께 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "로그인 사용자와 팀 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberVo.class))),
      @ApiResponse(responseCode = "500", description = "로그인 사용자와 팀 조회 실패", content = @Content(mediaType = "application/json", schema = @Schema()))
  })
  @GetMapping("/me/with-team")
  public MemberVo getMeWithTeam(@AuthenticationPrincipal JwtUserDetails user) {

    return memberService.getMemberTeamById(user.getId());
  }

  @Operation(summary = "모든 사용자 정보 조회", description = "모든 사용자 정보를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "모든 사용자 정보 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberListVo.class))),
      @ApiResponse(responseCode = "500", description = "모든 사용자 정보 조회 실패", content = @Content(mediaType = "application/json", schema = @Schema()))
  })
  @GetMapping()
  public MemberListVo getAllMember() {

    return memberService.getAllMemberInfo();
  }

  @Operation(summary = "아이디로 사용자 정보 조회", description = "아이디로 사용자 정보를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "아이디로 사용자 정보 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberVo.class))),
      @ApiResponse(responseCode = "500", description = "모든 사용자 정보 조회 실패", content = @Content(mediaType = "application/json", schema = @Schema()))
  })
  @GetMapping("/{id}")
  public MemberVo getMemberById(@PathVariable long id) {

    return memberService.getMemberById(id);
  }

  @Operation(summary = "json 속성:값으로 작성해 사용자 정보 조회", description = "복합 조건으로 사용자 정보를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "복합 조건으로 사용자 정보 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberVo.class))),
      @ApiResponse(responseCode = "500", description = "복합 조건으로 사용자 정보 조회 실패", content = @Content(mediaType = "application/json", schema = @Schema()))
  })
  @PostMapping("/search")
  public MemberVo searchMember(@RequestBody MemberDto request) {

    return memberService.getMemberInfo(request);
  }

  @Operation(summary = "그룹 필터 변환 없이 사용자와 팀 정보 조회", description = "복합 조건으로 사용자와 팀 정보를 함께 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "복합 조건으로 사용자와 팀 정보 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberTeamVo.class))),
      @ApiResponse(responseCode = "500", description = "복합 조건으로 사용자와 팀 정보 조회 실패", content = @Content(mediaType = "application/json", schema = @Schema()))
  })
  @PostMapping("/with-team/search")
  public List<MemberTeamVo> getMemberWithTeam(@RequestBody MemberDto request) {

    return memberService.getMemberTeamList(request);
  }

  @Operation(summary = "field filter 객체로 사용자와 팀 정보 조회", description = "동적 조건으로 사용자와 팀 정보를 함께 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "동적 조건으로 사용자와 팀 정보 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberTeamVo.class))),
      @ApiResponse(responseCode = "500", description = "동적 조건으로 사용자와 팀 정보 조회 실패", content = @Content(mediaType = "application/json", schema = @Schema()))
  })
  @PostMapping("/filter")
  public MemberListVo filterMember(@RequestBody MemberFilterDto request) {

    return memberService.filterMemberList(request);
  }

  @Operation(summary = "field filter 객체로 사용자와 팀 정보 페이징 조회", description = "동적 조건으로 사용자와 팀 정보를 함께 페이징 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "동적 조건으로 사용자와 팀 정보 페이징 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberTeamVo.class))),
      @ApiResponse(responseCode = "500", description = "동적 조건으로 사용자와 팀 정보 페이징 조회 실패", content = @Content(mediaType = "application/json", schema = @Schema()))
  })
  @PostMapping("/filter-paging")
  public PageResponse pagingFilterMember(@RequestBody MemberFilterDto request,
      Pageable pageable) {

    return memberService.filterMemberList(request, pageable);
  }

  @Operation(summary = "group filter 객체로 사용자와 팀 정보 페이징 조회", description = "그룹 조건으로 사용자와 팀 정보를 함께 페이징 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "그룹 조건으로 사용자와 팀 정보 페이징 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberTeamVo.class))),
      @ApiResponse(responseCode = "500", description = "그룹 조건으로 사용자와 팀 정보 페이징 조회 실패", content = @Content(mediaType = "application/json", schema = @Schema()))
  })
  @PostMapping("/filter-group-paging")
  public PageResponse filterGroupMemberPaging(@RequestBody SearchGroupDto request,
      Pageable pageable) {

    return memberService.filterMemberList(request, pageable);
  }
}
