package wise.study.prac.controller.view;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

  @GetMapping("/welcome")
  public String welcome(@AuthenticationPrincipal UserDetails user, Model model) {

    model.addAttribute("userName", user.getUsername());
    return "welcome";
  }
}
