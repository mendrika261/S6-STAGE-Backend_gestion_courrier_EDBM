package mg.edbm.mail.config.properties;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class FileUploadProperties {
    public String UPLOAD_DIR = "uploads";
    public long MAX_FILE_SIZE = 5 * 1024 * 1024; // 10 MB
    public String[] ALLOWED_FILE_TYPES = {"image/jpeg", "image/png", "image/gif", "image/bmp", "application/pdf"};

    public String[] getAllowedFileTypesExtensions() {
        return Arrays.stream(ALLOWED_FILE_TYPES).map(type -> type.split("/")[1]).toArray(String[]::new);
    }
}
