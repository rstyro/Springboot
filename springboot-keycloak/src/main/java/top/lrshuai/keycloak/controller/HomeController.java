package top.lrshuai.keycloak.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.lrshuai.keycloak.resp.R;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/public/hello")
    public R publicHello() {
        return R.ok("Hello from public endpoint!");
    }

    @GetMapping("/home")
    public R home(@AuthenticationPrincipal OidcUser user) {
        return R.ok(Map.of(
                "message", "Hello from secured endpoint!",
                "userName", user.getPreferredUsername(),
                "name", user.getFullName(),
                "email", user.getEmail(),
                "claims", user.getClaims()
        ));
    }

    @GetMapping("/public/logout-success")
    public R logoutSuccess() {
        return R.ok("Logout successful!");
    }
}
