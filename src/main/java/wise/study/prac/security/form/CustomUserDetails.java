package wise.study.prac.security.form;

import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetails extends UserDetails {

  long getId();
}
