package utilities.finalproject.web;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import utilities.finalproject.domain.User;
import utilities.finalproject.repository.UserRepository;
import utilities.finalproject.security.JwtService;
import utilities.finalproject.service.RefreshTokenService;
import utilities.finalproject.service.UserDetailsServiceImpl;

@Controller
public class SignUpSignInController {


    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private UserDetailsServiceImpl userDetailsService;

    private RefreshTokenService refreshTokenService;

    public SignUpSignInController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
                                  UserDetailsServiceImpl userDetailsService, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.refreshTokenService = refreshTokenService;
    }

    @GetMapping("/signup")
    public String signup(ModelMap model) {
        model.put("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/signup";
    }
    @GetMapping("/signin")
    public String signin() {
        return "signin";
    }

    @GetMapping("/signin-error")
    public String signupError(ModelMap model) {
        model.addAttribute("signinError", true);
        return "signin";
    }

//    @PostMapping("/signin")
//    public String signin(User user) {
//        User loggedInUser = (User) userDetailsService.loadUserByUsername(user.getUsername());
//        if (loggedInUser == null) return "redirect:/signin-error";
//        if (!passwordEncoder.matches(user.getPassword(), loggedInUser.getPassword())) return "redirect:/signin-error";
//        return "redirect:/";
//    }


}
