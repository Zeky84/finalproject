package utilities.finalproject.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import utilities.finalproject.domain.User;

@Controller
public class SignUpController {

    @GetMapping("/signup")
    public String signup(ModelMap model){
        model.put("user", new User());
        return "signup";
    }
    @PostMapping("/signup")
    public String signup(User user){
        return "redirect:/signin";
    }
}
