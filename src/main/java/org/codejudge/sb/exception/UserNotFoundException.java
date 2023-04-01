package org.codejudge.sb.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserNotFoundException extends RuntimeException {

    String message;

    public UserNotFoundException(String message) {
        super(message);
        this.message = message;
    }

}
