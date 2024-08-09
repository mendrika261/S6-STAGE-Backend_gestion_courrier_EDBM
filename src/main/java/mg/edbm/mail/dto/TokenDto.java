package mg.edbm.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import mg.edbm.mail.dto.response.UserResponse;
import mg.edbm.mail.entity.type.Token;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TokenDto {
    private UserResponse user;
    private String token;
    private LocalDateTime expiredAt;

    public TokenDto(Token token) {
        setUser(new UserResponse(token.getUser()));
        setToken(token.getValue());
        setExpiredAt(token.getExpiredAt());
    }
}
