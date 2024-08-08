package mg.edbm.mail.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import mg.edbm.mail.entity.Role;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.entity.type.UserStatus;

@Data
public class UserDtoResponse {
    private String id;
    private String lastName;
    private String firstName;
    private String email;
    private String phoneNumber;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;
    private String[] roles;
    private UserStatus status;

    public UserDtoResponse(User user) {
        setId(user.getId().toString());
        setLastName(user.getLastName());
        setFirstName(user.getFirstName());
        setEmail(user.getEmail());
        setPhoneNumber(user.getPhoneNumber());
        setRoles(user.getRoles().stream().map(Role::getCode).toArray(String[]::new));
        setStatus(user.getStatus());
    }

    public UserDtoResponse(User user, boolean withPassword) {
        this(user);
        if (withPassword) setPassword(user.getPassword());
    }
}
