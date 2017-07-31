package com.lemuelinchrist.exercise.facepalm.controllers.dto;

import com.lemuelinchrist.exercise.facepalm.exception.InvalidParameterException;

/**
 * @author Lemuel Cantos
 * @since 1/8/2017
 */
public class SenderDTO extends RequestDTO {
    private String sender;
    private String text;

    public SenderDTO(String sender, String text) {
        this.sender = sender;
        this.text = text;
    }

    public SenderDTO() {

    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * Ensures that this DTO Object sender field is not empty and that it is a properly
     * formed email.
     *
     * @throws InvalidParameterException Thrown if the emails in friends list do not meet the expected criteria.
     */
    public void checkValidity() throws InvalidParameterException {
        if (sender == null || sender.isEmpty()) throw new InvalidParameterException("Sender is Empty");
        if (!isValidEmail(sender)) throw new InvalidParameterException("Sender is not a valid email");


    }
}
