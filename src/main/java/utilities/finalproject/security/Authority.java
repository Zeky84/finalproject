package utilities.finalproject.security;

import org.springframework.security.core.GrantedAuthority;

public class Authority implements GrantedAuthority {

    private String authority;
    /*
    DON'T REMEMBER HOW IT WORKS THIS BUT IT'S RELATED TO THE USER ROLE
     */

    public Authority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
