package utilities.finalproject.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import utilities.finalproject.service.UserDetailsServiceImpl;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsServiceImpl userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //requests are made of:
        //header -> key:value pairs example: (Authorization -> Bearer xxx.yyy.zzz)
        //body -> if is JSON is has key:value pairs too
        String authorizationHeader = request.getHeader("Authorization"); // this is the header key that contains the token
        if ((StringUtils.hasText(authorizationHeader))) {//.hasText is equivalent to not null,if instead write not null, it will throw an error
            String token = authorizationHeader.substring(7); // this is to remove the "Bearer " part of the token and get only the token
            String subject = jwtService.extractSubject(token);//this is the username in the token
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (StringUtils.hasText(subject) && authentication == null) {// this is to check if the user is not authenticated
                UserDetails userDetails = userDetailsService.loadUserByUsername(subject);// this is to load the user from the database

                if (jwtService.validateToken(token, userDetails)) {// this is to validate the token
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();// this is to create an empty context
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                            userDetails.getPassword(), userDetails.getAuthorities());// this is to create the authentication token

                    securityContext.setAuthentication(authToken);// this is to set the security context with the authentication token
                    SecurityContextHolder.setContext(securityContext);// this is to set the security context in the security holder

                }

            }
        }
        filterChain.doFilter(request, response);
    }
}
