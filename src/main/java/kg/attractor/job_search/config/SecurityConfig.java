package kg.attractor.job_search.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final AuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureUrl("/auth/login?error=true")
                        .permitAll())
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .permitAll())
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/resumes/by-category/{categoryId}", "/api/respond").hasAuthority("APPLICANT")
                        .requestMatchers("/chat/{chatRoomId:[0-9]+}", "/chats").authenticated()
                        .requestMatchers("/chat/start/{otherUserId:[0-9]+}", "/applications").hasAnyAuthority("APPLICANT", "EMPLOYER")
                        .requestMatchers("/ws-chat/**").permitAll()
                        .requestMatchers("/webjars/**", "/js/**").permitAll()
                        .requestMatchers("/vacancies", "/vacancies/{vacancyId}").permitAll()
                        .requestMatchers("/profile", "/profile/edit", "/profile/**").authenticated()
                        .requestMatchers("/vacancies/**").hasAnyAuthority("ADMIN", "EMPLOYER")
                        .requestMatchers("/resumes").hasAnyAuthority("ADMIN", "EMPLOYER")
                        .requestMatchers("/resumes/create", "/resumes/{resumeId}/edit").hasAnyAuthority("ADMIN", "APPLICANT")
                        .anyRequest().permitAll())
                .csrf(csrf -> csrf.ignoringRequestMatchers(request -> {
                    String path = request.getServletPath();
                    return path.startsWith("/ws-chat/");
                }));
        return http.build();
    }

}