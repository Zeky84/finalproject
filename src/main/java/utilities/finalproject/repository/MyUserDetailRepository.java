package utilities.finalproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utilities.finalproject.domain.MyUserDetail;

public interface MyUserDetailRepository extends JpaRepository<MyUserDetail,Long> {

    MyUserDetail findByUsername(String username);
}
