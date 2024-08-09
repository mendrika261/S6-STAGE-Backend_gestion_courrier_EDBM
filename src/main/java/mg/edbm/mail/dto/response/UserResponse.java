package mg.edbm.mail.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import mg.edbm.mail.dto.LocationDto;
import mg.edbm.mail.entity.Location;
import mg.edbm.mail.entity.Role;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.entity.type.UserStatus;

import java.util.UUID;

@Data
public class UserResponse {
    private UUID id;
    private String lastName;
    private String firstName;
    private String email;
    private String phoneNumber;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;
    private String[] roles;
    private UserStatus status;
    private LocationDto location;

    public UserResponse(User user) {
        setId(user.getId());
        setLastName(user.getLastName());
        setFirstName(user.getFirstName());
        setEmail(user.getEmail());
        setPhoneNumber(user.getPhoneNumber());
        setRoles(user.getRoles().stream().map(Role::getCode).toArray(String[]::new));
        setStatus(user.getStatus());
        setLocation(new LocationDto(user.getLocation()));
    }
}
