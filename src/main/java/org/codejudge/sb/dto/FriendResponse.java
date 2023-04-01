package org.codejudge.sb.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class FriendResponse implements IUserResponse{
    List<String> friends;
}
