package mg.edbm.mail.service;

import lombok.RequiredArgsConstructor;
import mg.edbm.mail.config.properties.FileUploadProperties;
import mg.edbm.mail.dto.request.FileUploadRequest;
import mg.edbm.mail.entity.File;
import mg.edbm.mail.entity.Mail;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.exception.NotFoundException;
import mg.edbm.mail.repository.FileRepository;
import mg.edbm.mail.repository.MailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private static final Logger log = LoggerFactory.getLogger(FileService.class);
    private final FileRepository fileRepository;
    private final MailService mailService;
    private final FileUploadProperties fileUploadProperties;
    private final MailRepository mailRepository;

    private void deleteStoredFile(File file) throws IOException {
        Files.deleteIfExists(Paths.get(file.getPath()));
    }

    @Async
    public void storeFile(MultipartFile fileContent, File file) throws IOException {
        final String pathWithName = fileUploadProperties.UPLOAD_DIR + "/" + file.getId();
        Path copyLocation = Paths.get(pathWithName);
        Files.copy(fileContent.getInputStream(), copyLocation);
        file.setPath(copyLocation.toString());
        file.setName(fileContent.getOriginalFilename());
    }

    public File uploadMailFile(UUID mailId, FileUploadRequest fileUploadRequest, User authenticatedUser) throws IOException {
        final Mail mail = mailService.getIfSendBy(mailId, authenticatedUser);

        final File file = new File(fileUploadRequest.getFile(), mail, authenticatedUser);
        try {
            storeFile(fileUploadRequest.getFile(), file);
            mail.addFile(file);
            mailRepository.save(mail);
            return file;
        } catch (IOException e) {
            deleteStoredFile(file);
            throw e;
        }
    }

    public File getFile(UUID fileId) throws NotFoundException {
        return fileRepository.findById(fileId).orElseThrow(
                () -> new NotFoundException("File with id #" + fileId + " not found"));
    }

    public File downloadFile(UUID fileId) throws IOException, NotFoundException {
        final File file = getFile(fileId);
        final Path path = Paths.get(file.getPath());
        try {
            file.setResource(new ByteArrayResource(Files.readAllBytes(path)));
        } catch (NoSuchFileException e) {
            log.error("File with id #{} not found", fileId, e);
            throw new NotFoundException("File with id #" + fileId + " not found");
        }
        return file;
    }
}
