package utilities.finalproject.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import utilities.finalproject.domain.MyUserDetail;
import utilities.finalproject.repository.MyUserDetailRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MyUserDetailRepository myUserDetailRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        MyUserDetail userDetail =  myUserDetailRepository.findByUsername(username);
        if (userDetail == null) throw new UsernameNotFoundException("Bad Credentials");

        return userDetail;
    }
}
