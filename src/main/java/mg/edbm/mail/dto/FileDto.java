package mg.edbm.mail.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import mg.edbm.mail.entity.File;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class FileDto {
    private UUID id;
    private String name;
    private String type;
    private String path;

    public FileDto(File file) {
        setId(file.getId());
        setName(file.getName());
        setType(file.getType());
        setPath(file.getPath());
    }
}
