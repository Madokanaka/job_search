package kg.attractor.job_search.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kg.attractor.job_search.exception.UserNotFoundException;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.io.IOException;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    private final LocaleResolver localeResolver;
    private final MessageSource messageSource;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(messageSource.getMessage("error.user.not.found.withoutId", null, localeResolver.resolveLocale(request))));

        String languagePreference = user.getLanguagePreference();
        if (languagePreference != null && !languagePreference.isEmpty()) {
            Locale locale = new Locale(languagePreference);
            localeResolver.setLocale(request, response, locale);
            LocaleContextHolder.setLocale(locale);
        } else {
            localeResolver.setLocale(request, response, new Locale("ru"));
            LocaleContextHolder.setLocale(new Locale("ru"));
        }

        if (authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equalsIgnoreCase("APPLICANT"))) {
            response.sendRedirect("/vacancies");
        } else if (authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equalsIgnoreCase("EMPLOYER"))) {
            response.sendRedirect("/resumes");
        } else {
            response.sendRedirect("/profile");
        }
    }
}