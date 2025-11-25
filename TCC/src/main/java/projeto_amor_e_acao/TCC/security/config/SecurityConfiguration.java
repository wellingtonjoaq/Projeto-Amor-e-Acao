package projeto_amor_e_acao.TCC.security.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {

    private final CustomAuthSuccessHandler customAuthSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())

                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/imagens/**", "/visitantes", "/visitantes/**").permitAll()
                        .requestMatchers("/login", "/login/**", "/error/**").permitAll()
                        .requestMatchers(
                                "/curso/listarUsuarioSimples", "/curso/filtrarPesquisaUsuarioSimples", "/curso/filtrarUsuarioSimples",
                                "/aluno/listarUsuarioSimples", "/aluno/filtrarPesquisaUsuarioSimples", "/aluno/filtrarUsuarioSimples", "/aluno/visualizaUsuarioSimples/{id}",
                                "/voluntario/listarUsuarioSimples", "/voluntario/filtrarPesquisaUsuarioSimples", "/voluntario/filtrarUsuarioSimples", "/voluntario/visualizaUsuarioSimples/{id}",
                                "/relatorio/listarUsuarioSimples"
                        ).hasRole("USER")

                        .requestMatchers("/aluno", "/aluno/**",
                                "/curso", "/curso/**",
                                "/empresaParceira", "/empresaParceira/**",
                                "/funcao", "/funcao/**",
                                "/historico", "/historico/**",
                                "/usuario", "/usuario/**",
                                "/voluntario", "/voluntario/**",
                                "/relatorio", "/relatorio/**",
                                "/notificacao", "/notificacao/**",
                                "/upload", "/upload/**"
                        ).hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(customAuthSuccessHandler)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                        .logoutSuccessUrl("/login?logout=true")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout/visitantes", "GET"))
                        .logoutSuccessUrl("/visitantes/")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )

                .exceptionHandling(handler -> handler.accessDeniedPage("/access-denied"))

                .httpBasic(Customizer.withDefaults())

                .build();
    }

    // expõe AuthenticationManager se precisar injetar em algum serviço
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // encoder para senhas (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
