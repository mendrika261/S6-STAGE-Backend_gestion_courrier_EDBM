package mg.edbm.mail.controller;

import lombok.RequiredArgsConstructor;
import mg.edbm.mail.dto.AppDto;
import mg.edbm.mail.dto.request.ListRequest;
import mg.edbm.mail.entity.App;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.exception.AuthenticationException;
import mg.edbm.mail.service.AppService;
import mg.edbm.mail.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/apps")
@RequiredArgsConstructor
public class AppController {
    private final UserService userService;
    private final AppService appService;

    @GetMapping
    public ResponseEntity<List<AppDto>> listAuthorizedApps() throws AuthenticationException {
        final User user = userService.getAuthenticatedUser();
        final List<App> apps = appService.listAuthorizedApps(user);
        return ResponseEntity.ok(AppDto.from(apps));
    }
}
