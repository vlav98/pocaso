package com.nnk.springboot.config;

import com.nnk.springboot.services.UserDetailsServicesImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    /**
     * Creates and returns a bean of type UserDetailsService. This service is responsible for
     * loading user information from a data source based on the username provided
     * during authentication.
     *
     * @return a bean of type UserDetailsService.
     */
    @Bean
    public UserDetailsServicesImpl userDetailsService() {
        return new UserDetailsServicesImpl();
    }

    /**
     * Creates and returns a bean of type BCryptPasswordEncoder. This bean is used for encoding
     * user passwords before storing them in the data source. BCrypt is a secure password hashing algorithm.
     *
     * @return a bean of type BCryptPasswordEncoder.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates and returns a bean of type AuthenticationManager. This bean is responsible for
     * authenticating users based on the provided credentials (username and password).
     *
     * @return a bean of type AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        ProviderManager providerManager = new ProviderManager(daoAuthenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);

        return providerManager;
    }

    /**
     * Creates and returns a builder for configuring an MvcRequestMatcher bean. This matcher
     * can be used to specify URL patterns where specific security filters should be applied.
     *
     * @param handlerMappingIntrospector a HandlerMappingIntrospector bean
     * @return a builder for configuring an MvcRequestMatcher bean.
     */
    @Bean
    MvcRequestMatcher.Builder requestMatcher(HandlerMappingIntrospector handlerMappingIntrospector) {
        return new MvcRequestMatcher.Builder(handlerMappingIntrospector);
    }

    /**
     * Configures the security filter chain. This method is typically used for advanced customization
     * of the security filters applied to requests.
     * @param httpSecurity the HttpSecurity object used for configuring web security.
     * @param authenticationManager the AuthenticationManager used for user authentication.
     * @param mvcRequestMatcher an optional MVCRequestMatcher to restrict where the security filters
     *                          are applied.
     * @return the HttpSecurity object for further configuration.
     * @throws Exception if an error occurs while configuring the security filters.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager, MvcRequestMatcher.Builder mvcRequestMatcher) throws Exception {
        return httpSecurity
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .maximumSessions(1)
                        .expiredUrl("/login"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(mvcRequestMatcher.pattern("/css/**")).permitAll()
                        .requestMatchers(mvcRequestMatcher.pattern("/user/**")).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .defaultSuccessUrl("/")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/app-logout")
                        .logoutSuccessUrl("/login")
                )
                .exceptionHandling((exception -> exception.accessDeniedPage("/403")))
                .authenticationManager(authenticationManager)
                .build();
    }
}
