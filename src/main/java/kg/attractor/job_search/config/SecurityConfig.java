package kg.attractor.job_search.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .successHandler((request, response, authentication) -> {
                            if (authentication.getAuthorities().stream()
                                    .anyMatch(auth -> auth.getAuthority().equalsIgnoreCase("APPLICANT"))) {
                                response.sendRedirect("/vacancies");
                            } else if (authentication.getAuthorities().stream()
                                    .anyMatch(auth -> auth.getAuthority().equalsIgnoreCase("EMPLOYER"))) {
                                response.sendRedirect("/resumes");
                            } else {
                                response.sendRedirect("/profile");
                            }
                        })
                        .failureUrl("/auth/login?error=true")
                        .permitAll())
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .permitAll())
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize

                                .requestMatchers("/vacancies").permitAll()
                                .requestMatchers("/profile", "/profile/edit").authenticated()
                                .requestMatchers("/vacancies/**", "/resumes").hasAnyAuthority("ADMIN", "EMPLOYER")
                                .requestMatchers("/resumes/create", "/resumes/{resumeId}/edit").hasAnyAuthority("ADMIN", "APPLICANT")
                                .anyRequest().permitAll()

                );
                return http.build();
    }

}
