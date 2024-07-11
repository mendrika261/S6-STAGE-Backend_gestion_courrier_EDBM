package mg.edbm.gestion_courrier.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.edbm.gestion_courrier.service.TokenService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TokenRequestFilter extends OncePerRequestFilter {
    private final TokenService tokenService;

    public TokenRequestFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        tokenService.authentifierUtilisateur(request);
        chain.doFilter(request, response);
    }
}
