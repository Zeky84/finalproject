package utilities.finalproject.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import utilities.finalproject.domain.MyUserDetail;
import utilities.finalproject.repository.MyUserDetailRepository;

@RestController
@RequestMapping("/api/v1")
public class UserDetailController {

    @Autowired
    private MyUserDetailRepository myUserDetailRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/users")
    public ResponseEntity<MyUserDetail> createUser(@RequestBody MyUserDetail myUserDetail){
        MyUserDetail newUserDetail = new MyUserDetail(myUserDetail.getUsername(),passwordEncoder.encode(myUserDetail.getPassword()));

        MyUserDetail savedUser = myUserDetailRepository.save(newUserDetail);

        return ResponseEntity.ok(savedUser);
    }
}
