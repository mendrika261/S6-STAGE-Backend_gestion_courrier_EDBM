package mg.edbm.mail.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.edbm.mail.analysis.AnalysisRequest;
import mg.edbm.mail.analysis.AnalysisResult;
import mg.edbm.mail.analysis.MailAnalysis;
import mg.edbm.mail.analysis.MouvementAnalysis;
import mg.edbm.mail.config.SecurityConfig;
import mg.edbm.mail.dto.request.ListRequest;
import mg.edbm.mail.dto.response.MouvementResponse;
import mg.edbm.mail.entity.Mouvement;
import mg.edbm.mail.service.MouvementService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/mouvements")
@RestController
@Transactional
@RequiredArgsConstructor
public class MouvementController {
    private final MouvementService mouvementService;

    @GetMapping
    @Secured(SecurityConfig.ROLE_ADMIN)
    public ResponseEntity<Page<MouvementResponse>> list(ListRequest listRequest) {
        final Page<Mouvement> mouvements = mouvementService.list(listRequest);
        return ResponseEntity.ok(mouvements.map(MouvementResponse::new));
    }

    @GetMapping("/analysis")
    public ResponseEntity<MouvementAnalysis> analyze() {
        return ResponseEntity.ok(new MouvementAnalysis());
    }

    @PostMapping("/analysis")
    public ResponseEntity<AnalysisResult> analyze(@Valid MouvementAnalysis mouvementAnalysis) {
        return ResponseEntity.ok(mouvementService.analyzeMouvement(mouvementAnalysis));
    }
}
