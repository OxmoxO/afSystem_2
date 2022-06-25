package antifraud.service;

import antifraud.dao.UserDao;
import antifraud.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class UserService implements UserDetailsService {

   @Autowired
    private UserDao userDao;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao
                .findByUsernameIgnoreCase(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User" + username + " is NOT found!"));
    }

    public void register(User user) {
        if (userDao
                .findByUsernameIgnoreCase(user.getUsername())
                .isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        user.setPassword(passwordEncoder
                                        .encode(user.getPassword()));
        userDao.save(user);
    }

    public Map<String, String> delete(String username) {

        User user = userDao
                           .findByUsernameIgnoreCase(username)
                           .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));


        userDao
                .deleteById(user.getId());
        return Map.of("username", username,
                "status", "Deleted successfully!");
    }

    public List<User> getAll() {
        return userDao.findAll();
    }
}
