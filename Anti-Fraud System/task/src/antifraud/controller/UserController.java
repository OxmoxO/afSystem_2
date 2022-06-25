package antifraud.controller;

import antifraud.model.User;
import antifraud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private  UserService userService;

    @PostMapping("/api/auth/user")
    public ResponseEntity addUser(@RequestBody User user) {

        if (user.getUsername()==null ||
                user.getPassword()==null ||
                user.getName()==null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        userService.register(user);
        return new ResponseEntity(user, HttpStatus.CREATED);
    }

    @GetMapping("/api/auth/list")
    public List<User> getAllUser() {
        return userService.getAll();
    }

    @DeleteMapping("/api/auth/user/{username}")
    public Map<String, String> deleteUser(@PathVariable String username) {
        return userService.delete(username);
    }
}