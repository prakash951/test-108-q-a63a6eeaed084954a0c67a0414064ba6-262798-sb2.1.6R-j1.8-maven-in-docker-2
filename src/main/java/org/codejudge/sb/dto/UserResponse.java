package org.codejudge.sb.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse implements IUserResponse{
    String username;
}
