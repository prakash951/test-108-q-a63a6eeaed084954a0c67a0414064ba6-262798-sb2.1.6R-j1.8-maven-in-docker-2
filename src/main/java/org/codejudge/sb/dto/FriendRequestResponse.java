package org.codejudge.sb.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FriendRequestResponse implements IUserResponse{
    String status;
}
