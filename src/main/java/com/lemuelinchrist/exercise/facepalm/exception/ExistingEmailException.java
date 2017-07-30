package com.lemuelinchrist.exercise.facepalm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Lemuel Cantos
 * @since 30/7/2017
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Email Already Exists")
public class ExistingEmailException extends Exception {
}