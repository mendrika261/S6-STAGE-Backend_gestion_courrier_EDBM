package mg.edbm.gestion_courrier.service;

import lombok.RequiredArgsConstructor;
import mg.edbm.gestion_courrier.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
}
