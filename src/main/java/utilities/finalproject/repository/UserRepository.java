package utilities.finalproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utilities.finalproject.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
