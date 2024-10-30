package mg.edbm.mail.dto.request;

import lombok.Data;
import mg.edbm.mail.dto.request.validator.ValidFile;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileUploadRequest {
    @ValidFile
    private MultipartFile file;
}
