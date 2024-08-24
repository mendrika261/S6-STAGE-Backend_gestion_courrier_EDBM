package mg.edbm.mail.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class FileUploadProperties {
    @Value("${file.upload.dir}")
    public String UPLOAD_DIR;
    @Value("${file.upload.max-size}")
    public long MAX_FILE_SIZE;
    @Value("${file.upload.allowed-types}")
    public String[] ALLOWED_FILE_TYPES;

    public String[] getAllowedFileTypesExtensions() {
        return Arrays.stream(ALLOWED_FILE_TYPES).map(type -> type.split("/")[1]).toArray(String[]::new);
    }
}
