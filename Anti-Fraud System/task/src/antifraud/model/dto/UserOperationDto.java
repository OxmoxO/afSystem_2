package antifraud.model.dto;

import antifraud.model.annotation.OperationCorrect;
import antifraud.model.enums.Operation;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Data
public class UserOperationDto {

    @NotEmpty
    private String username;
    @OperationCorrect(enumClazz = Operation.class,
                      message = "Operation must be LOCK or UNLOCK!")
    private String operation;
}