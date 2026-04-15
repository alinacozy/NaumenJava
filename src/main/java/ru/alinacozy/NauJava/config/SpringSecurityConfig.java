package ru.alinacozy.NauJava.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig
{

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    @Bean
    public PasswordEncoder getPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        // отключаем csrf- защиту для reports, чтобы тестировать вне браузера
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/reports/**"))
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/registration", "/login", "/logout")
                        .permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/reports/**"
                        ).hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/custom/floss/view/list", true)
                        .permitAll()
                )
                // для тестирования запросов к API через curl/postman
                .httpBasic(httpBasic -> {})
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                // Пробрасываем AccessDeniedException в ExceptionControllerAdvice (чтобы вернуть единообразное исключение)
                .exceptionHandling(exceptions -> exceptions
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            handlerExceptionResolver.resolveException(request, response, null, accessDeniedException);
                        })
                );
        return http.build();
    }


}
