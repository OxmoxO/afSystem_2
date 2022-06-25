package antifraud.dao;

import antifraud.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends CrudRepository<User, Long> {

    List<User> findAll();

    Optional<User> findByUsernameIgnoreCase(String username);
}
