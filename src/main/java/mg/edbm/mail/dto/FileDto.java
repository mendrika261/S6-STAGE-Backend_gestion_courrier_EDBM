package mg.edbm.mail.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import mg.edbm.mail.entity.File;
import mg.edbm.mail.utils.NumberUtils;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class FileDto {
    private UUID id;
    private String name;
    private String type;
    private Long size;
    private String humanReadableSize;
    private String path;

    public FileDto(File file) {
        setId(file.getId());
        setName(file.getName());
        setType(file.getType());
        setSize(file.getSize());
        setPath("files/" + file.getId().toString());
    }

    public void setSize(Long size) {
        this.size = size;
        setHumanReadableSize(NumberUtils.octetToHumanReadable(size));
    }
}
