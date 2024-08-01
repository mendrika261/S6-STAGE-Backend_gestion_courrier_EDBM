package mg.edbm.gestion_courrier.controller;

import mg.edbm.gestion_courrier.service.RoleService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }
}
