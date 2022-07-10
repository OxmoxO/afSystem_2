package antifraud.model.dto;


import antifraud.model.annotation.RoleCorrect;
import antifraud.model.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Data
public class ChangeRoleDto {

    @NotEmpty
    private String username;

    @RoleCorrect(enumClazz = Role.class, message = "Role incorrect")
    private String role;
}