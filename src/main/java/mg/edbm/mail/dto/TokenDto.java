package mg.edbm.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import mg.edbm.mail.entity.type.Token;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TokenDto {
    private UserDto user;
    private String token;
    private LocalDateTime expiredAt;

    public TokenDto(Token token) {
        setUser(new UserDto(token.getUser()));
        setToken(token.getValue());
        setExpiredAt(token.getExpiredAt());
    }
}
