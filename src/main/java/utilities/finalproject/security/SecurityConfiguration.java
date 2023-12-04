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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder(){// It's being set in the authenticationProvider
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsServiceMethod() {// It's being set in the authenticationProvider
        return new UserDetailsServiceImpl();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)//disabling this to allow sending raw data from postman
                .authorizeHttpRequests((request) -> {
                    request.requestMatchers("admin","logintest2").authenticated()
                            .requestMatchers("/api/v1/users").permitAll();
                })
                .authenticationProvider(authenticationProviderMethod())
                .formLogin(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProviderMethod (){
        DaoAuthenticationProvider daoAuthenticationProvider  = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());//Setting the passwordEncoder
        daoAuthenticationProvider.setUserDetailsService(userDetailsServiceMethod()); // Setting the userDetailsService
        return daoAuthenticationProvider;
    }
}
