package utilities.finalproject.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import utilities.finalproject.repository.UserRepository;
import utilities.finalproject.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    /*
    this is the class to handle the security configuration
     */

    private UserRepository userRepository;
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(UserRepository userRepository, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userRepository = userRepository;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {// It's being set in the authenticationProvider
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsServiceMethod() {// It's being set in the authenticationProvider
        return new UserDetailsServiceImpl(userRepository);
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)//disabling this to allow sending raw data from postman
                .authorizeHttpRequests((request) -> {
                    request
                            .requestMatchers(
                                    "admin",
                                    "logintest2").authenticated()
                            .requestMatchers("/api/v1/users",
                                    "/signup",
                                    "/api/v1/users/",
                                    "/api/v1/users/login",
                                    "/api/v1/users/refreshtoken").permitAll();
                })
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProviderMethod())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(signin -> {
                    signin.loginPage("/signin");
                    signin.failureForwardUrl("/signin-error");
                    signin.permitAll();
                });
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProviderMethod() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());//Setting the passwordEncoder
        daoAuthenticationProvider.setUserDetailsService(userDetailsServiceMethod()); // Setting the userDetailsService
        return daoAuthenticationProvider;
    }
}
