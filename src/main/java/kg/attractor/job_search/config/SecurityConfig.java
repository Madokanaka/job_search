package kg.attractor.job_search.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
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
        log.info("Fetching user with query: {}", fetchUser);
        log.info("Fetching roles with query: {}", fetchRoles);
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(fetchUser)
                .authoritiesByUsernameQuery(fetchRoles);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .defaultSuccessUrl("/vacancies", true)
                        .failureUrl("/auth/login?error=true")
                        .permitAll())
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .permitAll())
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/register", "/vacancies", "/vacancies/{vacancyId}", "/", "/auth/**").permitAll()
//                        .requestMatchers("/applications/**", "/resumes/create", "/resumes/{resumeId}/edit", "/resumes/{resumeId}/delete", "/profile/**", "/user/{userId}/employee").hasAnyAuthority("APPLICANT", "ADMIN")
//
//                        .requestMatchers("/vacancies/create", "/vacancies/{vacancyId}/edit", "/vacancies/{vacancyId}/delete", "/vacancies/category/{categoryId}","/user/{userId}/applicant", "/profile/**").hasAnyAuthority("EMPLOYER", "ADMIN")
//
//                        .requestMatchers("/images/**").hasAnyAuthority("EMPLOYER", "ADMIN", "APPLICANT")
//
//                        .requestMatchers("/users/**", "/vacancies/**", "/resumes/**", "/api/**").hasAuthority("ADMIN")
                                .requestMatchers("/vacancies").permitAll()
                                .requestMatchers("/profile", "/profile/edit").authenticated()
                                .requestMatchers("/vacancies/**", "/resumes").hasAnyAuthority("ADMIN", "EMPLOYER")
                                .requestMatchers("/resumes/create", "/resumes/{resumeId}/edit").hasAnyAuthority("ADMIN", "APPLICANT")
                                .anyRequest().permitAll()

//                        .anyRequest().authenticated()
                );
                return http.build();
    }

}
