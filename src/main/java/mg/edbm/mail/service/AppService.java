package mg.edbm.mail.service;

import lombok.RequiredArgsConstructor;
import mg.edbm.mail.entity.App;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.repository.AppRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppService {
    private final AppRepository appRepository;

    public List<App> listAuthorizedApps(User user) {
        List<App> apps = appRepository.findAllActive();
        return Arrays.asList(apps.stream()
                .filter(app -> user.getRoles().stream()
                        .anyMatch(role -> role.getCode().startsWith(app.getAuthorizedRolePrefix())))
                .toArray(App[]::new));
    }
}
