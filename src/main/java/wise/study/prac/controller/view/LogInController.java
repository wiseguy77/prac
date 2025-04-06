package wise.study.prac.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogInController {

  @GetMapping("/")
  public String root() {
    return "login";
  }

//  @GetMapping("/login")
//  public String login_jwt() {
//
//    return "login";
//  }
}
