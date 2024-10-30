package mg.edbm.mail.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mg.edbm.mail.dto.AppDto;
import mg.edbm.mail.dto.request.ListRequest;
import mg.edbm.mail.dto.request.filter.SpecificationImpl;
import mg.edbm.mail.dto.request.type.OperationType;
import mg.edbm.mail.entity.App;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.exception.NotFoundException;
import mg.edbm.mail.repository.AppRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
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

    public Page<App> list(ListRequest listRequest) {
        listRequest.addBaseCriteria("removedAt", OperationType.EQUAL, null);
        final Pageable pageable = listRequest.toPageable();
        final Specification<App> specification = new SpecificationImpl<>(listRequest);
        return appRepository.findAll(specification, pageable);
    }

    public App create(AppDto appDto, User author) {
        final App app = new App(appDto, author);
        final Optional<App> removedApp = appRepository.findIfRemoved(app.getName(), app.getUrl());
        if(removedApp.isPresent()) {
            final App removed = removedApp.get();
            removed.update(appDto, author);
            removed.restore(author);
            log.info("{} restored {}", author, removed);
            return appRepository.save(removed);
        }
        log.info("{} created {}", author, app);
        return appRepository.save(app);
    }

    public App get(UUID appId) throws NotFoundException {
        return appRepository.findById(appId).orElseThrow(
                () -> new NotFoundException("L'application #" + appId + " n'existe pas")
        );
    }

    public App update(UUID appId, AppDto appDto, User author) throws NotFoundException {
        final App app = get(appId);
        app.update(appDto, author);
        log.info("{} updated {}", author, app);
        return appRepository.save(app);
    }

    public App delete(UUID appId, User author) throws NotFoundException {
        final App app = get(appId);
        app.delete(author);
        log.info("{} deleted {}", author, app);
        return appRepository.save(app);
    }
}
