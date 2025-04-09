package wise.study.prac.mvc.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wise.study.prac.mvc.dto.MemberInfoAllResponse;
import wise.study.prac.mvc.dto.MemberInfoResponse;
import wise.study.prac.mvc.dto.RegisterMemberRequest;
import wise.study.prac.mvc.service.MemberService;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  @PostMapping()
  public void register(@RequestBody RegisterMemberRequest request) {

    memberService.registerMember(request);
  }

  @GetMapping()
  public MemberInfoResponse getMember(@AuthenticationPrincipal UserDetails user)
      throws NotFoundException {

    return memberService.getMemberInfo(user.getUsername());
  }

  @GetMapping("/all")
  public MemberInfoAllResponse getAllMember() {

    return memberService.getAllMemberInfo();
  }
}
