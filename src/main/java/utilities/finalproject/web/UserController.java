package utilities.finalproject.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utilities.finalproject.domain.RefreshToken;
import utilities.finalproject.domain.User;
import utilities.finalproject.repository.UserRepository;
import utilities.finalproject.request.RefreshTokenRequest;
import utilities.finalproject.response.AuthenticationResponse;
import utilities.finalproject.response.RefreshTokenResponse;
import utilities.finalproject.security.JwtService;
import utilities.finalproject.service.RefreshTokenService;
import utilities.finalproject.service.UserDetailsServiceImpl;

import java.util.HashMap;

@Controller
@RequestMapping("/api/v1/users")
public class UserController {
    //this will be the signup controller

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private UserDetailsServiceImpl userDetailsService;

    private RefreshTokenService refreshTokenService;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
                          UserDetailsServiceImpl userDetailsService, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.refreshTokenService = refreshTokenService;
    }


    @PostMapping("")
    @ResponseBody
    public ResponseEntity<AuthenticationResponse> signUpUser(@RequestBody User userRequest) {

        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        User savedUser = userRepository.save(userRequest);
        String userSignUpToken = jwtService.generateJwtToken(new HashMap<>(), savedUser);// this is an access token
        String refreshToken = refreshTokenService.generateRefreshToken(savedUser.getId()).getRefreshToken();// this is a refresh token
        //right now we're responding with the token and the username, in the future we'll respond with the role too
        return ResponseEntity.ok(new AuthenticationResponse(userSignUpToken,refreshToken, savedUser.getUsername()));
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<AuthenticationResponse> signInUser(@RequestBody User user) {
        User loggedInUser = (User) userDetailsService.loadUserByUsername(user.getUsername());// this is to load a user already created from the database
        String userSignInToken = jwtService.generateJwtToken(new HashMap<>(), loggedInUser);// this is an access token
        String refreshToken = refreshTokenService.generateRefreshToken(loggedInUser.getId()).getRefreshToken();// this is a refresh token
        return ResponseEntity.ok(new AuthenticationResponse(userSignInToken,refreshToken, loggedInUser.getUsername()));
    }

    @PostMapping("/refreshtoken")
    @ResponseBody
    public ResponseEntity<RefreshTokenResponse> getNewAccessToken(@RequestBody RefreshToken refreshToken){
        String refreshTokenString = refreshToken.getRefreshToken();
        String newAccessToken = refreshTokenService.generateAccessToken(refreshToken);
        return ResponseEntity.ok(new RefreshTokenResponse(refreshTokenString,newAccessToken));
    }


}
