package com.lemuelinchrist.exercise.facepalm.controllers.dto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Lemuel Cantos
 * @since 31/7/2017
 */
public abstract class RequestDTO {

    protected boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(email);
        if (!mat.matches()) {
            return false;
        } else {
            return true;
        }
    }
}
