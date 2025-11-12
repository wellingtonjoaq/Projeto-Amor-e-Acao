package projeto_amor_e_acao.TCC.middleware;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import projeto_amor_e_acao.TCC.service.LogAuditoriaService;

import java.security.Principal;

@Component
public class LogAuditoriaInterceptor implements HandlerInterceptor {

    @Autowired
    private LogAuditoriaService logAuditoriaService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception
    {
        Principal userPrincipal = request.getUserPrincipal();

        if (userPrincipal != null && request.getMethod().matches("POST|PUT|DELETE")) {
            String usuarioIdStr = userPrincipal.getName();
            Long usuarioId = Long.parseLong(usuarioIdStr);

            String acao = request.getMethod() + " " + request.getRequestURI();

            logAuditoriaService.registrarLog(usuarioId, acao);
        }

        return true;
    }
}
