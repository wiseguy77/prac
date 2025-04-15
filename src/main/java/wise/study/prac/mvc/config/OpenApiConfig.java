package wise.study.prac.mvc.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
    info = @Info(
        title = "My Template Project",
        version = "v1",
        description = "My Template Project API Documentation",
        contact = @Contact(name = "wise", email = "ultrawise77@gmail.com")
    )
)
public class OpenApiConfig {

}
