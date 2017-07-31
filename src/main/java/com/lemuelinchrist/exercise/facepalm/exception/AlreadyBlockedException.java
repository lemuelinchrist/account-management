package com.lemuelinchrist.exercise.facepalm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when Account is already blocked
 *
 * @author Lemuel Cantos
 * @since 31/7/2017
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Target already blocked")
public class AlreadyBlockedException extends Exception {
}
