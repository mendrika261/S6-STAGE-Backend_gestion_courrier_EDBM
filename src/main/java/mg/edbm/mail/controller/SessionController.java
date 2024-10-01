package mg.edbm.mail.controller;

import lombok.RequiredArgsConstructor;
import mg.edbm.mail.config.SecurityConfig;
import mg.edbm.mail.dto.SessionDto;
import mg.edbm.mail.dto.request.ListRequest;
import mg.edbm.mail.entity.Session;
import mg.edbm.mail.entity.type.SessionStatus;
import mg.edbm.mail.service.SessionService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
@Transactional
public class SessionController {
    private final SessionService sessionService;

    @GetMapping
    @Secured(SecurityConfig.ROLE_ADMIN)
    public ResponseEntity<Page<SessionDto>> list(ListRequest listRequest) {
        final Page<Session> sessions = sessionService.list(listRequest);
        return ResponseEntity.ok(sessions.map(SessionDto::new));
    }

    @GetMapping("/stats")
    @Secured(SecurityConfig.ROLE_ADMIN)
    public ResponseEntity<HashMap<String, ?>> stats() {
        final HashMap<String, Object> response = new HashMap<>();
        response.put("activeSessionCount", sessionService.countSessionsByType(SessionStatus.ACTIVE));
        response.put("tentativeSessionCount", sessionService.countSessionsByType(SessionStatus.TENTATIVE));
        response.put("intrusionSessionCount", sessionService.countSessionsByType(SessionStatus.INTRUSION));
        response.put("averageQueryCountPerSession", sessionService.averageQueryCountPerSession());
        return ResponseEntity.ok(response);
    }
}
