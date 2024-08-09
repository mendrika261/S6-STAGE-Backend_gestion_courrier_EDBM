package mg.edbm.mail.service;

import lombok.RequiredArgsConstructor;
import mg.edbm.mail.dto.request.ListRequest;
import mg.edbm.mail.dto.request.filter.SpecificationImpl;
import mg.edbm.mail.dto.request.type.OperationType;
import mg.edbm.mail.entity.Mail;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.exception.NotFoundException;
import mg.edbm.mail.repository.MailRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailService {
    private final MailRepository mailRepository;
    private final UserService userService;

    public Page<Mail> getMailByUser(UUID id, ListRequest listRequest) throws NotFoundException {
        final User user = userService.get(id);
        listRequest.addCriteria("createdBy", OperationType.EQUAL, user);
        final Specification<Mail> specification = new SpecificationImpl<>(listRequest.getCriteria());
        final Pageable pageable = listRequest.toPageable();
        return mailRepository.findAll(specification, pageable);
    }
}
