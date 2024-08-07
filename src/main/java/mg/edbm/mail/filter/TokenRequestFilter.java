package mg.edbm.mail.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.edbm.mail.exception.AuthenticationException;
import mg.edbm.mail.service.TokenService;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TokenRequestFilter extends OncePerRequestFilter {
    private final TokenService tokenService;

    public TokenRequestFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain
    ) throws ServletException, IOException {
        tokenService.authenticateUser(request);
        chain.doFilter(request, response);
    }
}
