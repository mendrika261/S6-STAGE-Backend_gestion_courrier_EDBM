package mg.edbm.mail.controller;

import lombok.RequiredArgsConstructor;
import mg.edbm.mail.service.MouvementService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mouvements")
@RequiredArgsConstructor
public class MouvementController {
    private final MouvementService mouvementService;
}
