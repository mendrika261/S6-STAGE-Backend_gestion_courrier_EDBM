package mg.edbm.mail.dto;

import lombok.Data;
import mg.edbm.mail.entity.Role;
import mg.edbm.mail.entity.User;

@Data
public class UserDto {
    private String id;
    private String lastName;
    private String firstName;
    private String email;
    private String phoneNumber;
    private String[] roles;

    public UserDto(User utilisateur) {
        setId(utilisateur.getId().toString());
        setLastName(utilisateur.getLastName());
        setFirstName(utilisateur.getFirstName());
        setEmail(utilisateur.getEmail());
        setPhoneNumber(utilisateur.getPhoneNumber());
        setRoles(utilisateur.getRoles().stream().map(Role::getCode).toArray(String[]::new));
    }
}
