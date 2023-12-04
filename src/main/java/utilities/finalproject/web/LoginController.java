package utilities.finalproject.web;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class LoginController {

    @GetMapping("/logintest1")
    @ResponseBody
    public ResponseEntity<String>  in() {
        return ResponseEntity.ok("This is a test, test, test");
    }

    @GetMapping("/logintest2")
    @ResponseBody
    public String in1() {
        return "This is another test, test, test, the default security is not protecting this endpoint";
    }

    @GetMapping("/logintest3")
    public ResponseEntity<String> in2() {
        return ResponseEntity.ok("This is another test, test, test");
    }

}
