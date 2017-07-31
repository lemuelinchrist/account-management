package com.lemuelinchrist.exercise.facepalm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when Befriending is attempted but one account is blocking the other.
 *
 * @author Lemuel Cantos
 * @since 1/8/2017
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Cannot befriend. One Account blocked the other")
public class AccountBlockedException extends Exception {
}
