package mg.edbm.mail.controller;

import lombok.RequiredArgsConstructor;
import mg.edbm.mail.config.SecurityConfig;
import mg.edbm.mail.dto.AppDto;
import mg.edbm.mail.dto.request.ListRequest;
import mg.edbm.mail.entity.App;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.exception.AuthenticationException;
import mg.edbm.mail.exception.NotFoundException;
import mg.edbm.mail.service.AppService;
import mg.edbm.mail.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/apps")
@RequiredArgsConstructor
public class AppController {
    private final UserService userService;
    private final AppService appService;

    @GetMapping("/authorized")
    public ResponseEntity<List<AppDto>> listAuthorizedApps() throws AuthenticationException {
        final User user = userService.getAuthenticatedUser();
        final List<App> apps = appService.listAuthorizedApps(user);
        return ResponseEntity.ok(AppDto.from(apps));
    }

    @GetMapping
    @Secured(SecurityConfig.ROLE_ADMIN)
    public ResponseEntity<Page<AppDto>> list(ListRequest listRequest) {
        final Page<App> apps = appService.list(listRequest);
        return ResponseEntity.ok(apps.map(AppDto::new));
    }

    @GetMapping("/{appId}")
    @Secured(SecurityConfig.ROLE_ADMIN)
    public ResponseEntity<AppDto> get(@PathVariable UUID appId) throws NotFoundException {
        final App app = appService.get(appId);
        return ResponseEntity.ok(new AppDto(app));
    }

    @PostMapping
    @Secured(SecurityConfig.ROLE_ADMIN)
    public ResponseEntity<AppDto> create(AppDto appDto) throws AuthenticationException {
        final User user = userService.getAuthenticatedUser();
        final App app = appService.create(appDto, user);
        return ResponseEntity.ok(new AppDto(app));
    }

    @PutMapping("/{appId}")
    @Secured(SecurityConfig.ROLE_ADMIN)
    public ResponseEntity<AppDto> update(@PathVariable UUID appId, AppDto appDto) throws AuthenticationException, NotFoundException {
        final User user = userService.getAuthenticatedUser();
        final App app = appService.update(appId, appDto, user);
        return ResponseEntity.ok(new AppDto(app));
    }

    @DeleteMapping("/{appId}")
    @Secured(SecurityConfig.ROLE_ADMIN)
    public ResponseEntity<AppDto> delete(@PathVariable UUID appId) throws AuthenticationException, NotFoundException {
        final User user = userService.getAuthenticatedUser();
        final App app = appService.delete(appId, user);
        return ResponseEntity.ok(new AppDto(app));
    }
}
