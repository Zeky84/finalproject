package utilities.finalproject.web;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AdminController {

    @GetMapping("/admin")
    @ResponseBody
    public ResponseEntity<String> admin() {
        return ResponseEntity.ok("THIS IS AN ADMIN ENDPOINT TESTTTTTTTTT");
    }
}
