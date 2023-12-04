package utilities.finalproject.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import utilities.finalproject.domain.MyUserDetail;

@Controller
public class SignUpController {



    @GetMapping("/signup")
    public String signup(ModelMap model){
        model.put("myUserDetail", new MyUserDetail());
        return "signup";
    }
}
