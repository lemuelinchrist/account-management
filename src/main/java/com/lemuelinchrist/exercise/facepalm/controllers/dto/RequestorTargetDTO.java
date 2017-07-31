package com.lemuelinchrist.exercise.facepalm.controllers.dto;

import com.lemuelinchrist.exercise.facepalm.exception.InvalidParameterException;

/**
 * @author Lemuel Cantos
 * @since 31/7/2017
 */
public class RequestorTargetDTO extends RequestDTO {
    private String requestor;
    private String target;

    public String getRequestor() {
        return requestor;
    }

    public void setRequestor(String requestor) {
        this.requestor = requestor;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * Ensures that this DTO Object fields Target and Requestor are not empty and that they are properly
     * formed emails.
     *
     * @throws InvalidParameterException Thrown if the emails in friends list do not meet the expected criteria.
     */
    public void checkValidity() throws InvalidParameterException {
        if (requestor == null || requestor.isEmpty()) throw new InvalidParameterException("Requestor is Empty");
        if (!isValidEmail(requestor)) throw new InvalidParameterException("Request is not a valid email");
        if (target == null || target.isEmpty()) throw new InvalidParameterException("Target is Empty");
        if (!isValidEmail(target)) throw new InvalidParameterException("Target is not a valid email");


    }
}
