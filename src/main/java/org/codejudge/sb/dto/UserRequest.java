package org.codejudge.sb.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UserRequest {
    
    @NotNull(message = "Username should not be null")
    @NotBlank(message = "Username should not be blank")
    String username;
}
