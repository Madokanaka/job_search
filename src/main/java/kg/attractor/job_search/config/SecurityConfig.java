package kg.attractor.job_search.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        String fetchUser = "select email, password, enabled " +
                "from USERS " +
                "where email = ?";
        String fetchRoles = "select email, role " +
                "from USERS u, roles r " +
                "where u.email = ? " +
                "and u.role_id = r.id ";
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(fetchUser)
                .authoritiesByUsernameQuery(fetchRoles);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/register", "/vacancies", "vacancies/category/{categoryId}", "vacancies/{vacancyId}").permitAll()

                        .requestMatchers("/applications/**", "/resumes", "/resumes/create", "/resumes/{resumeId}/edit", "/profile/**").hasAnyRole("APPLICANT", "ADMIN")

                        .requestMatchers("/vacancies/", "vacancies/{vacancyId}/edit", "vacancies/{vacancyId}/delete", "/user/{userId}/employee", "users/responded/{vacancyId}", "/profile/**").hasAnyRole("EMPLOYER", "ADMIN")

                        .requestMatchers("/users/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                );
                return http.build();
    }

}
