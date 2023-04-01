package org.codejudge.sb.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorUserResponse implements IUserResponse
{
    String reason;
    String status;
}