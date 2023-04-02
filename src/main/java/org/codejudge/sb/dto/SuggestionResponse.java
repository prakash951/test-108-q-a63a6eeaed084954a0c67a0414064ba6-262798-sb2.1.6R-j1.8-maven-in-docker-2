package org.codejudge.sb.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class SuggestionResponse implements IUserResponse{
    List<String> suggestions;
}
