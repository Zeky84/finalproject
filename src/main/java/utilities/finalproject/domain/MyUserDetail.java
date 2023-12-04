package utilities.finalproject.domain;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


@Entity
@Table(name = "myusersdetail")
public class MyUserDetail implements UserDetails {// make this class to extend from User
    /**
     * This class is used to implement the UserDetailService interface
     *
     * @return UserDetails Obj set with user.password and user.username
     */

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

//    private User user; // this is the user that will be used to authenticate the user later on


    public MyUserDetail() {
    }

    public MyUserDetail(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;// user.getPassword();
    }

    @Override
    public String getUsername() {
        return username;// user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
