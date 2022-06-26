package antifraud.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
@Entity
@Table(name = "user")
@NoArgsConstructor
@Getter
@Setter
public class User  implements Serializable {
    @Column
    private String name;
    @Column
    private String username;
    @Column
    private String password;
    @Id
    @GeneratedValue(strategy =  GenerationType.SEQUENCE)
    private Long id;
    @Column
    private String role;
    @Column
    private boolean isNonLocked;

    public User(String name, String username, String password, String role) {
            this.name = name;
            this.username = username;
            this.password = password;
            this.role = role;
            isNonLocked = this.role.equals("ROLE_ADMINISTRATOR");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            User user = (User) o;
            return username.equals(user.username);
        }
        @Override
        public int hashCode() {
            return Objects.hash(username);
        }
    }


