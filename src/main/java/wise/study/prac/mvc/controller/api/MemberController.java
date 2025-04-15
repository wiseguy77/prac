package wise.study.prac.mvc.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
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

  @PostMapping()
  public void register(@RequestBody RegisterMemberRequest request) {

    memberService.registerMember(request);
  }

  @GetMapping("/me")
  public MemberResponse getMe(@AuthenticationPrincipal JwtUserDetails user)
      throws NotFoundException {

    MemberSvcParam param = MemberRequest.builder().account(user.getAccount()).build();
    return memberService.getMemberInfo(param);
  }

  @GetMapping("/me/with-team")
  public MemberResponse getMeWithTeam(@AuthenticationPrincipal JwtUserDetails user) {

    return memberService.getMemberTeamInfo(MemberRequest.builder()
        .account(user.getAccount()).build());
  }

  @GetMapping()
  public MemberAllResponse getAllMember() {

    return memberService.getAllMemberInfo();
  }

  @GetMapping("/{id}")
  public MemberResponse getMemberById(@PathVariable int id) {

    return memberService.getMemberById(id);
  }

  @PostMapping("/search")
  public MemberResponse searchMember(@RequestBody MemberRequest request) {

    return memberService.getMemberInfo(request);
  }

  @PostMapping("/with-team/search")
  public MemberTeamResponse getMemberTeam(@RequestBody MemberRequest request) {

    return memberService.getMemberTeamInfo(request);
  }
}
