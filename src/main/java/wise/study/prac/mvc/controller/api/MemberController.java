package wise.study.prac.mvc.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wise.study.prac.mvc.dto.MemberAllResponse;
import wise.study.prac.mvc.dto.MemberRequest;
import wise.study.prac.mvc.dto.MemberResponse;
import wise.study.prac.mvc.dto.MemberTeamResponse;
import wise.study.prac.mvc.dto.RegisterMemberRequest;
import wise.study.prac.mvc.service.MemberService;
import wise.study.prac.mvc.service.params.MemberSvcParam;
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
  public void register(@RequestBody RegisterMemberRequest request) {

    memberService.registerMember(request);
  }

  @Operation(summary = "로그인 사용자 정보 조회", description = "현재 로그인 중인 사용자의 정보를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "로그인 사용자 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberResponse.class))),
      @ApiResponse(responseCode = "500", description = "로그인 사용자 조회 실패", content = @Content(mediaType = "application/json", schema = @Schema()))
  })
  @GetMapping("/me")
  public MemberResponse getMe(@AuthenticationPrincipal JwtUserDetails user) {

    MemberSvcParam param = MemberRequest.builder().account(user.getAccount()).build();
    return memberService.getMemberInfo(param);
  }

  @Operation(summary = "로그인 사용자와 팀 정보 조회", description = "현재 로그인 중인 사용자와 팀 정보를 함께 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "로그인 사용자와 팀 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberResponse.class))),
      @ApiResponse(responseCode = "500", description = "로그인 사용자와 팀 조회 실패", content = @Content(mediaType = "application/json", schema = @Schema()))
  })
  @GetMapping("/me/with-team")
  public MemberResponse getMeWithTeam(@AuthenticationPrincipal JwtUserDetails user) {

    return memberService.getMemberTeamInfo(MemberRequest.builder()
        .account(user.getAccount()).build());
  }

  @Operation(summary = "모든 사용자 정보 조회", description = "모든 사용자 정보를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "모든 사용자 정보 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberAllResponse.class))),
      @ApiResponse(responseCode = "500", description = "모든 사용자 정보 조회 실패", content = @Content(mediaType = "application/json", schema = @Schema()))
  })
  @GetMapping()
  public MemberAllResponse getAllMember() {

    return memberService.getAllMemberInfo();
  }

  @Operation(summary = "아이디로 사용자 정보 조회", description = "아이디로 사용자 정보를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "아이디로 사용자 정보 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberResponse.class))),
      @ApiResponse(responseCode = "500", description = "모든 사용자 정보 조회 실패", content = @Content(mediaType = "application/json", schema = @Schema()))
  })
  @GetMapping("/{id}")
  public MemberResponse getMemberById(@PathVariable int id) {

    return memberService.getMemberById(id);
  }

  @Operation(summary = "복합 조건으로 사용자 정보 조회", description = "복합 조건으로 사용자 정보를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "복합 조건으로 사용자 정보 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberResponse.class))),
      @ApiResponse(responseCode = "500", description = "복합 조건으로 사용자 정보 조회 실패", content = @Content(mediaType = "application/json", schema = @Schema()))
  })
  @PostMapping("/search")
  public MemberResponse searchMember(@RequestBody MemberRequest request) {

    return memberService.getMemberInfo(request);
  }

  @Operation(summary = "복합 조건으로 사용자와 팀 정보 조회", description = "복합 조건으로 사용자와 팀 정보를 함께 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "복합 조건으로 사용자와 팀 정보 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberTeamResponse.class))),
      @ApiResponse(responseCode = "500", description = "복합 조건으로 사용자와 팀 정보 조회 실패", content = @Content(mediaType = "application/json", schema = @Schema()))
  })
  @PostMapping("/with-team/search")
  public MemberTeamResponse getMemberTeam(@RequestBody MemberRequest request) {

    return memberService.getMemberTeamInfo(request);
  }
}
