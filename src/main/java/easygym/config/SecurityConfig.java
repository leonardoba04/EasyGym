package easygym.config;

import easygym.model.Aluno;
import easygym.model.Funcionario;
import easygym.repository.Database;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Optional;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final Database db;

    public SecurityConfig(Database db) { this.db = db; }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/uploads/**", "/login").permitAll()
                .requestMatchers("/aluno/**").hasRole("ALUNO")
                .requestMatchers("/academia/**", "/funcionarios/**").hasRole("ADMIN")
                .anyRequest().hasAnyRole("ADMIN", "FUNCIONARIO")
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(new SimpleUrlAuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest req,
                            HttpServletResponse res, Authentication auth) throws IOException {
                        boolean isAluno = auth.getAuthorities()
                            .contains(new SimpleGrantedAuthority("ROLE_ALUNO"));
                        res.sendRedirect(isAluno ? "/aluno/home" : "/");
                    }
                })
                .failureUrl("/login?erro=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?saiu=true")
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/login?acesso=negado")
            );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // Tenta como funcionário (login por email)
            Optional<Funcionario> func = db.buscarFuncionarioPorEmail(username);
            if (func.isPresent()) {
                Funcionario f = func.get();
                if (!f.isAtivo()) throw new UsernameNotFoundException("Inativo");
                String role = f.isAdmin() ? "ROLE_ADMIN" : "ROLE_FUNCIONARIO";
                return User.withUsername(f.getEmail())
                    .password(f.getSenha())
                    .authorities(new SimpleGrantedAuthority(role))
                    .build();
            }
            // Tenta como aluno (matrícula — normaliza maiúsculas e espaços)
            String matriculaNorm = username.toUpperCase().trim();
            Optional<Aluno> aluno = db.buscarAlunoPorMatricula(matriculaNorm);
            if (aluno.isPresent()) {
                Aluno a = aluno.get();
                return User.withUsername(a.getMatricula())
                    .password(a.getSenha())
                    .authorities(new SimpleGrantedAuthority("ROLE_ALUNO"))
                    .build();
            }
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }
}
