package com.lemuelinchrist.exercise.facepalm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when the Email in the request doesn't exist in the database
 * @author Lemuel Cantos
 * @since 30/7/2017
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Account Does Not Exist")
public class NonExistentAccountException extends Exception {
}
