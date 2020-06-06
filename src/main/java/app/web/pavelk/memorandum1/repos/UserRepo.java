package app.web.pavelk.memorandum1.repos;

import app.web.pavelk.memorandum1.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
}