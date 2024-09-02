package mg.edbm.mail.service;

import lombok.RequiredArgsConstructor;
import mg.edbm.mail.entity.Mail;
import mg.edbm.mail.entity.Mouvement;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.exception.NotFoundException;
import mg.edbm.mail.repository.MailRepository;
import mg.edbm.mail.repository.MouvementRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MouvementService {
    private final MouvementRepository mouvementRepository;
    private final MailService mailService;
    private final MailRepository mailRepository;

    public Mouvement createMouvement(UUID mailId, User messenger) throws NotFoundException {
        final Mail mail = mailService.get(mailId);
        final Mouvement mouvement = new Mouvement(mail, messenger);
        mail.addMouvement(mouvement);
        mailRepository.save(mail);
        return mouvement;
    }

    public Mouvement removeLastMouvement(UUID mailId, User messenger) throws NotFoundException {
        final Mail mail = mailService.get(mailId);
        final Mouvement mouvement = mail.removeLastMouvement();
        if(!mouvement.getMessenger().getId().equals(messenger.getId()))
            throw new NotFoundException("The last mouvement is not from the messenger");
        mailRepository.save(mail);
        return mouvement;
    }
}
