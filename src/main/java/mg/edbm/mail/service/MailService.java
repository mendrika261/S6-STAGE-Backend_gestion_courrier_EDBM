package mg.edbm.mail.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mg.edbm.mail.analysis.AnalysisResult;
import mg.edbm.mail.analysis.ChartData;
import mg.edbm.mail.analysis.Input;
import mg.edbm.mail.analysis.MailAnalysis;
import mg.edbm.mail.config.SecurityConfig;
import mg.edbm.mail.config.properties.FileUploadProperties;
import mg.edbm.mail.config.properties.NotificationUrlProperties;
import mg.edbm.mail.dto.MapDto;
import mg.edbm.mail.dto.request.MouvementRequest;
import mg.edbm.mail.dto.request.type.LogicOperationType;
import mg.edbm.mail.dto.request.FileUploadRequest;
import mg.edbm.mail.dto.request.ListRequest;
import mg.edbm.mail.dto.request.MailRequest;
import mg.edbm.mail.dto.request.filter.SpecificationImpl;
import mg.edbm.mail.dto.request.type.MailType;
import mg.edbm.mail.dto.request.type.OperationType;
import mg.edbm.mail.dto.request.type.SortType;
import mg.edbm.mail.entity.*;
import mg.edbm.mail.entity.type.MailStatus;
import mg.edbm.mail.entity.type.MouvementStatus;
import mg.edbm.mail.entity.type.NotificationType;
import mg.edbm.mail.exception.NotFoundException;
import mg.edbm.mail.repository.FileRepository;
import mg.edbm.mail.repository.MailRepository;
import mg.edbm.mail.repository.MouvementRepository;
import mg.edbm.mail.utils.StringCustomUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class MailService {
    private static final Integer MAIL_REFERENCE_SEQ_LENGTH = 6;
    private final MailRepository mailRepository;
    private final UserService userService;
    private final LocationService locationService;
    private final FileRepository fileRepository;
    private final FileService fileService;
    private final FileUploadProperties fileUploadProperties;
    private final MouvementRepository mouvementRepository;
    private final NotificationService notificationService;
    private final NotificationUrlProperties notificationUrlProperties;
    private final MapService mapService;
    private final JdbcTemplate jdbcTemplate;

    public Page<Mail> getMailsByUser(UUID userId, MailType type, ListRequest listRequest) {
        if (type == MailType.INCOMING)
            listRequest.addBaseCriteria("receiverUser.id", OperationType.EQUAL, userId);
        else
            listRequest.addBaseCriteria("senderUser.id", OperationType.EQUAL, userId);
        final Specification<Mail> specification = new SpecificationImpl<>(listRequest);
        final Pageable pageable = listRequest.toPageable();
        return mailRepository.findAll(specification, pageable);
    }

    private String getNextReference(MailType type) {
        // ENV2021/02/000001 OR --/REC2021/02/000001
        final String prefix = type.getReferencePrefix();
        final String year = String.valueOf(LocalDateTime.now().getYear());
        final String month = String.format("%02d", LocalDateTime.now().getMonthValue());
        final String lastReference = mailRepository.findLastReference(prefix);
        if (lastReference == null) {
            return prefix + year + "/" + month + "/" + String.format("%0"+ MAIL_REFERENCE_SEQ_LENGTH +"d", 1);
        }
        final String[] parts = lastReference.split("/");
        final int sequence = Integer.parseInt(parts[2]) + 1;
        return prefix + year + "/" + month + "/" + String.format("%0"+ MAIL_REFERENCE_SEQ_LENGTH +"d", sequence);
    }

    private Mail setReceiverMail(MailRequest mailRequest, Mail mail) throws NotFoundException {
        Location locationReceiver;
        String receiver;
        if (mailRequest.getReceiverUserId() != null) {
            final User receiverUser = userService.get(mailRequest.getReceiverUserId());
            mail.setReceiverUser(receiverUser);
            locationReceiver = receiverUser.getLocation();
            receiver = receiverUser.getFullName();
        } else {
            locationReceiver = locationService.get(mailRequest.getReceiverLocationId());
            receiver = mailRequest.getReceiver();
        }
        mail.setReceiverLocation(locationReceiver);
        mail.setReceiver(receiver);
        return mailRepository.save(mail);
    }

    @Transactional
    public Mail createIncomingMail(UUID userId, MailRequest mailRequest, User authenticatedUser)
            throws NotFoundException {
        final User senderUser = userService.get(userId);
        Mail mail = new Mail(mailRequest, senderUser, authenticatedUser);

        mail.setReference(getNextReference(MailType.INCOMING));
        mail.setSender(mailRequest.getSender());
        mail.setSenderLocation(locationService.get(mailRequest.getSenderLocationId()));

        mail = setReceiverMail(mailRequest, mail);

        // First mouvement to reception
        final Mouvement mouvement = new Mouvement(mail, authenticatedUser);
        mouvement.setReceiverUser(authenticatedUser);
        mouvement.setReceiver(authenticatedUser.getFullName());
        mouvement.setReceiverLocation(authenticatedUser.getLocation());
        mouvement.setStatus(MouvementStatus.DONE);
        mouvement.setEndDate(LocalDateTime.now());
        mail.addMouvement(mouvement);
        putDirections(mail.getId());
        return mailRepository.save(mail);
    }

    public Mail createOutgoingMail(UUID userId, MailRequest mailRequest, User authenticatedUser)
            throws NotFoundException {
        final User senderUser = userService.get(userId);
        final Mail mail = new Mail(mailRequest, senderUser, authenticatedUser);

        mail.setReference(getNextReference(MailType.OUTGOING));

        return setReceiverMail(mailRequest, mail);
    }

    public Mail createMail(UUID userId, MailRequest mailRequest, User authenticatedUser)
            throws NotFoundException {
        if (authenticatedUser.hasRole(SecurityConfig.ROLE_RECEPTIONIST)) {
            return createIncomingMail(userId, mailRequest, authenticatedUser);
        } else {
            return createOutgoingMail(userId, mailRequest, authenticatedUser);
        }
    }

    public Mail get(UUID mailId) throws NotFoundException {
        return mailRepository.findById(mailId).orElseThrow(
                () -> new NotFoundException("Le courrier n'existe pas"));
    }

    public Page<File> listFiles(UUID mailId, ListRequest listRequest) throws NotFoundException {
        final Mail mail = get(mailId);
        listRequest.addBaseCriteria("mail.id", OperationType.EQUAL, mail.getId());
        final Specification<File> specification = new SpecificationImpl<>(listRequest);
        final Pageable pageable = listRequest.toPageable();
        return fileRepository.findAll(specification, pageable);
    }

    public File uploadMailFile(UUID mailId, FileUploadRequest fileUploadRequest, User authenticatedUser) throws IOException {
        final Mail mail = getIfSendBy(mailId, authenticatedUser);

        final File file = new File(fileUploadRequest.getFile(), authenticatedUser);
        file.setPath(fileUploadProperties.UPLOAD_DIR + "/" + file.getId());
        fileService.storeFile(fileUploadRequest.getFile(), file.getPath());
        mail.addFile(file);
        mailRepository.save(mail);
        return file;
    }

    public Mail deleteMailFileSendBy(UUID mailId, UUID fileId, User author) throws NotFoundException {
        final Mail mail = getIfSendBy(mailId, author);
        final File file = fileRepository.findById(fileId).orElseThrow(
                () -> new NotFoundException("File with id #" + fileId + " not found"));

        try {
            mail.removeFile(file);
            mailRepository.save(mail);
            fileService.deleteStoredFile(file);
            fileRepository.delete(file);
        } catch (IOException e) {
            log.error("Error while deleting on storage file with id #{}", fileId, e);
        }
        return mail;
    }

    public void deleteAllMailFilesSendBy(UUID mailId, User author) throws NotFoundException {
        for (File file : getIfSendBy(mailId, author).getFiles()) {
            deleteMailFileSendBy(mailId, file.getId(), author);
        }
    }

    public Mail getIfSendBy(UUID mailId, User senderUser) {
        return mailRepository.findByIdAndSenderUser(mailId, senderUser).orElseThrow(
                () -> new AccessDeniedException("Vous n'êtes pas autorisé à accéder à ce courrier")
        );
    }

    public Mail getMailByUser(UUID userId, UUID mailId) throws NotFoundException {
        final User user = userService.get(userId);
        if(user.hasRole(SecurityConfig.ROLE_ADMIN)) return get(mailId);
        return mailRepository.findByIdAndReceiverOrSenderUser(mailId, user).orElseThrow(
                () -> new NotFoundException("Le courrier n'existe pas")
        );
    }

    private Mail updateOutgoingMail(UUID userId, UUID mailId, @Valid MailRequest mailRequest,
                                   User authenticatedUser) throws NotFoundException {
        final Mail mail = getIfSendBy(mailId, userService.get(userId));
        mail.updateIfAuthorized(mailRequest, authenticatedUser);
        if(mailRequest.getReceiverUserId() == null) {
            mail.setReceiverUser(null);
            mail.setReceiverLocation(locationService.get(mailRequest.getReceiverLocationId()));
            mail.setReceiver(mailRequest.getReceiver());
        } else {
            mail.setReceiverUser(userService.get(mailRequest.getReceiverUserId()));
            mail.setReceiverLocation(mail.getReceiverUser().getLocation());
            mail.setReceiver(mail.getReceiverUser().getFullName());
        }
        return mailRepository.save(mail);
    }

    private Mail updateIncomingMail(UUID userId, UUID mailId, @Valid MailRequest mailRequest,
                                   User authenticatedUser) throws NotFoundException {
        final Mail mail = updateOutgoingMail(userId, mailId, mailRequest, authenticatedUser);
        mail.setSender(mailRequest.getSender());
        mail.setSenderLocation(locationService.get(mailRequest.getSenderLocationId()));
        return mailRepository.save(mail);
    }

    public Mail updateMail(UUID userId, UUID mailId, @Valid MailRequest mailRequest,
                           User authenticatedUser) throws NotFoundException {
        if (authenticatedUser.hasRole(SecurityConfig.ROLE_RECEPTIONIST)) {
            return updateIncomingMail(userId, mailId, mailRequest, authenticatedUser);
        } else {
            return updateOutgoingMail(userId, mailId, mailRequest, authenticatedUser);
        }
    }

    public Mail updateMailStatus(UUID userId, UUID mailId, MailStatus mailStatus, User authenticatedUser)
            throws NotFoundException {
        final Mail mail = getIfSendBy(mailId, userService.get(userId));
        mail.updateStatusIfAuthorized(mailStatus, authenticatedUser);
        return mailRepository.save(mail);
    }

    public Mail deleteMail(UUID userId, UUID mailId) throws NotFoundException {
        final Mail mail = getIfSendBy(mailId, userService.get(userId));
        deleteAllMailFilesSendBy(mailId, userService.get(userId));
        mailRepository.delete(mail);
        return mail;
    }

    public Page<Mail> listWaitingMails(@Valid ListRequest listRequest) {
        listRequest.addBaseCriteria(LogicOperationType.AND, "status", OperationType.EQUAL, MailStatus.WAITING); // TODO make waiting when done
        listRequest.addOrder("priority", SortType.DESC);
        listRequest.addOrder("createdAt", SortType.ASC);
        final Specification<Mail> specification = new SpecificationImpl<>(listRequest);
        final Pageable pageable = listRequest.toPageable();
        return mailRepository.findAll(specification, pageable);
    }

    private Mail getIfMouvementSendingBy(UUID mailId, User senderUser) {
        return mailRepository.findByIdAndMouvementsSenderUser(mailId, senderUser).orElseThrow(
                () -> new AccessDeniedException("Vous n'êtes pas autorisé à accéder à ce courrier")
        );
    }

    public Mail signMouvementStart(UUID mailId, LocalDateTime startDate, User author) {
        final Mail mail = getIfMouvementSendingBy(mailId, author);
        mail.signMouvementStart(startDate);
        return mailRepository.save(mail);
    }

    public Page<Mail> listDeliveringMails(@Valid ListRequest listRequest, User messenger) {
        listRequest.addBaseCriteria("mouvements.messenger.id", OperationType.EQUAL, messenger.getId());
        listRequest.addBaseCriteria("mouvements.status", OperationType.EQUAL, MouvementStatus.DELIVERING);
        listRequest.addOrder("priority", SortType.DESC);
        listRequest.addOrder("mouvements.startDate", SortType.ASC);
        final Specification<Mail> specification = new SpecificationImpl<>(listRequest);
        final Pageable pageable = listRequest.toPageable();
        return mailRepository.findAll(specification, pageable);
    }

    private Mail getIfMouvementDeliveringBy(UUID mailId, User messenger) {
        return mailRepository.findByIdAndMouvementsMessengerAndMouvementsStatus(mailId, messenger, MouvementStatus.DELIVERING).orElseThrow(
                () -> new AccessDeniedException("Vous n'êtes pas autorisé à accéder à ce courrier")
        );
    }

    private Mail getIfMouvementReceivedBy(UUID mailId, User receiverUser) {
        return mailRepository.findByIdAndMouvementsReceiverUser(mailId, receiverUser).orElseThrow(
                () -> new AccessDeniedException("Vous n'êtes pas autorisé à accéder à ce courrier")
        );
    }

    public Mail deliverMail(UUID mailId, LocalDateTime endDate, User messenger) {
        final Mail mail = getIfMouvementDeliveringBy(mailId, messenger);
        mail.deliverMail(endDate);
        if(mail.getLastMouvement().getReceiverUser() != null)
            notificationService.create(NotificationType.DANGER,
                    "Passation courrier " + mail.getReference(),
                    "venant de " + StringCustomUtils.titleCase(messenger.getFirstName()),
                    mail.isAtDestination() ?
                    notificationUrlProperties.MAIL_PREVIEW.replace(":mailId", mail.getId().toString()):
                            notificationUrlProperties.MAIL_TRANSIT,
                    mail.getLastMouvement().getReceiverUser());
        if(mail.getLastMouvement().getReceiverUser() == null && mail.isAtDestination())
            notificationService.create(NotificationType.DANGER,
                    "Courrier livré " + mail.getReference(),
                    "pour " + StringCustomUtils.titleCase(mail.getReceiver()),
                    notificationUrlProperties.MAIL_PREVIEW.replace(":mailId", mail.getId().toString()),
                    mail.getSenderUser());
        return mailRepository.save(mail);
    }

    public Mail confirmDelivery(UUID mailId, LocalDateTime endDate, User receiverUser) {
        final Mail mail = getIfMouvementReceivedBy(mailId, receiverUser);
        mail.confirmDelivery(endDate);
        if(mail.isAtDestination())
            notificationService.create(NotificationType.DANGER,
                    "Courrier livré " + mail.getReference(),
                    "pour " + StringCustomUtils.titleCase(mail.getReceiver()),
                    notificationUrlProperties.MAIL_PREVIEW.replace(":mailId", mail.getId().toString()),
                    mail.getSenderUser());
        return mailRepository.save(mail);
    }

    @Async
    @Transactional
    public void putDirections(UUID mailId) throws NotFoundException {
        final Mail mail = get(mailId);
        final Mouvement mouvement = mail.getLastMouvement();
        if(mouvement == null || mouvement.getSenderLocation() == null || mouvement.getReceiverLocation() == null)
            return;
        final MapDto mapDto = mapService.getDirections(
                mouvement.getSenderLocation().getLatitude(),
                mouvement.getSenderLocation().getLongitude(),
                mouvement.getReceiverLocation().getLatitude(),
                mouvement.getReceiverLocation().getLongitude());
        mouvement.setEstimatedDelay(mapDto.getDuration());
        mouvement.setEstimatedDistance(mapDto.getDistance());
        mouvement.setCoordinates(mapDto.getCoordinates());
        mouvementRepository.save(mouvement);
    }

    @Transactional
    public Mouvement reroute(UUID mailId, MouvementRequest mouvementRequest, User messenger) throws NotFoundException {
        final Mail mail = getIfMouvementDeliveringBy(mailId, messenger);
        final Mouvement mouvement = mail.getLastMouvement();
        if(mouvementRequest.getReceiverUserId() == null) {
            mouvement.setReceiverUser(null);
            mouvement.setReceiver(mouvementRequest.getReceiver());
            mouvement.setReceiverLocation(locationService.get(mouvementRequest.getReceiverLocationId()));
        } else {
            final User user = userService.get(mouvementRequest.getReceiverUserId());
            mouvement.setReceiverUser(user);
            mouvement.setReceiver(user.getFullName());
            mouvement.setReceiverLocation(user.getLocation());
        }
        putDirections(mailId);
        return mouvementRepository.save(mouvement);
    }

    public Mail cancelDelivery(UUID mailId, User messenger) {
        final Mail mail = getIfMouvementDeliveringBy(mailId, messenger);
        mail.setStatus(MailStatus.DELIVERING);
        mail.getLastMouvement().setStatus(MouvementStatus.DELIVERING);
        mail.getLastMouvement().setEndDate(null);
        if(mail.getLastMouvement().getReceiverUser() != null)
            notificationService.create(NotificationType.INFO,
                    "Passation courrier annulé " + mail.getReference(),
                    "venant de " + StringCustomUtils.titleCase(messenger.getFirstName()),
                    mail.isAtDestination() ?
                            notificationUrlProperties.MAIL_PREVIEW.replace(":mailId", mail.getId().toString()):
                            notificationUrlProperties.MAIL_TRANSIT,
                    mail.getLastMouvement().getReceiverUser());
        return mailRepository.save(mail);
    }

    public Page<Mail> list(ListRequest listRequest) {
        final Pageable pageable = listRequest.toPageable();
        final Specification<Mail> specification = new SpecificationImpl<>(listRequest);
        return mailRepository.findAll(specification, pageable);
    }

    public String buildQuery(List<Input> columns, List<Input> mesures, List<Input> ordres, Long limit) {
        final StringBuilder query = new StringBuilder("SELECT ");
        for (Input column : columns) {
            query.append(column.getValue()).append(", ");
        }
        for (Input mesure : mesures) {
            query.append(mesure.getValue()).append(", ");
        }
        query.deleteCharAt(query.length() - 2);
        query.append(" FROM mail m left join location ls on m.sender_location_id = ls.id " +
                "left join location lr on m.receiver_location_id = lr.id " +
                "left join mouvement mv on m.id = mv.mail_id ");

        query.append(" GROUP BY mv.start_date, ");
        for (Input column : columns) {
            query.append(column.getValue()).append(", ");
        }
        query.deleteCharAt(query.length() - 2);

        query.append(" ORDER BY mv.start_date desc, ");
        if (ordres !=null && !ordres.isEmpty()) {
            for (Input ordre : ordres) {
                query.append(ordre.getValue()).append(", ");
            }
        }
        query.deleteCharAt(query.length() - 2);

        if (limit != null) {
            query.append(" LIMIT ").append(limit);
        } else {
            query.append(" LIMIT 100");
        }

        return query.toString();
    }

    public String getChartType(List<String> columns, List<Input> mesures) {
        // ref: https://www.coursera.org/learn/visualize-data/supplement/XvN2U/data-grows-on-decision-trees
        if(columns.stream().anyMatch(c -> c.toLowerCase().contains("localisation"))) {
            return "map";
        } else if(columns.stream().anyMatch(c -> c.toLowerCase().contains("date"))) {
            return "line";
        } else {
            return "bar";
        }
    }

    public List<String> getChartCategories(List<Input> columns, List<List<String>> data) {
        final List<String> categories = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            if(columns.size() == 1) {
                categories.add(data.get(i).get(0));
            } else
                categories.add("Ligne " + i+1);
        }
        return categories;
    }

    public List<ChartData> getChartData(List<Input> columns, List<Input> mesures, List<List<String>> data) {
        final List<ChartData> chartData = new ArrayList<>();
        final int columnMesureStart = columns.size();
        for (int col=columnMesureStart; col<columnMesureStart+mesures.size(); col++) {
            final List<String> values = new ArrayList<>();
            final String mesureName = mesures.get(col-columnMesureStart).getName();
            for (List<String> datum : data) {
                values.add(datum.get(col));
            }
            chartData.add(new ChartData(mesureName, values.toArray(String[]::new)));
        }
        return chartData;
    }

    public AnalysisResult buildResult(String query, List<Input> columns, List<Input> mesures) {
        final List<String> columnNames = new ArrayList<>();
        for (Input column : columns) {
            columnNames.add(column.getName());
        }
        for (Input mesure : mesures) {
            columnNames.add(mesure.getName());
        }
        jdbcTemplate.execute("SET lc_time = 'fr_FR'");
        final List<List<String>> data = jdbcTemplate.query(query, (rs, rowNum) -> {
            final List<String> row = new ArrayList<>();
            int end = columnNames.size();
            for (int i = 1; i <= end; i++) {
                if(rs.getMetaData().getColumnName(i).contains("latitude")) {
                    row.set(i - 2, row.get(i - 2) + " LAT" + rs.getString(i));
                    end += 1;
                } else if (rs.getMetaData().getColumnName(i).contains("longitude")) {
                    row.set(i - 3, row.get(i - 3) + "LONG" + rs.getString(i));
                    end += 1;
                }
                else row.add(rs.getString(i));
            }
            return row;
        });
        return new AnalysisResult(
                columnNames,
                data,
                getChartType(columnNames, mesures),
                getChartCategories(columns, data),
                getChartData(columns, mesures, data)
        );
    }

    public AnalysisResult analyzeMail(MailAnalysis mailAnalysis) {
        final String query = buildQuery(mailAnalysis.getColumns(), mailAnalysis.getMeasures(), mailAnalysis.getOrders(), mailAnalysis.getLimit());
        return buildResult(query, mailAnalysis.getColumns(), mailAnalysis.getMeasures());
    }
}
