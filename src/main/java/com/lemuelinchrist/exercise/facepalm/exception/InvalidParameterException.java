package com.lemuelinchrist.exercise.facepalm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thown when request parameters are not following the format. eg. malformed email or empty email.
 *
 * @author Lemuel Cantos
 * @since 30/7/2017
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Wrong Parameters")
public class InvalidParameterException extends Exception {
    public InvalidParameterException(String message) {
        super(message);
    }
}
