package org.codejudge.sb.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NoFriendRequestsPendingException extends RuntimeException {
    String message;

    public NoFriendRequestsPendingException(String message) {
        super(message);
        this.message = message;
    }
}
