package projeto_amor_e_acao.TCC.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import projeto_amor_e_acao.TCC.middleware.LogAuditoriaInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LogAuditoriaInterceptor logAuditoriaInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logAuditoriaInterceptor)
                .addPathPatterns("/api/**");
    }
}
