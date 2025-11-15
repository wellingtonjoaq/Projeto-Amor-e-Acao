package projeto_amor_e_acao.TCC.security.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();

        if (roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
            response.sendRedirect("/usuario/listar");
            return;
        }

        if (roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_USER"))) {
            response.sendRedirect("/curso/listarUsuarioSimples");
            return;
        }

        response.sendRedirect("/login");
    }
}
