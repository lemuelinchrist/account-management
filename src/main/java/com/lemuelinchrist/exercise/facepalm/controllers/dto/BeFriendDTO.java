package com.lemuelinchrist.exercise.facepalm.controllers.dto;

import com.lemuelinchrist.exercise.facepalm.exception.InvalidParameterException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Lemuel Cantos
 * @since 30/7/2017
 */
public class BeFriendDTO {
    private List<String> friends;

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public void checkValidity() throws InvalidParameterException {
        if (friends.size() != 2) throw new InvalidParameterException("You should only specify two emails");
        final boolean emptyEmails;
        for (String email : friends) {
            if (email.isEmpty()) throw new InvalidParameterException("One of the emails is an empty email");
        }
        final StringBuilder invalidEmails = new StringBuilder();
        friends.forEach(email -> {
            Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
            Matcher mat = pattern.matcher(email);
            if (!mat.matches()) {
                invalidEmails.append(email);
                invalidEmails.append(", ");
            }
        });
        if (!invalidEmails.toString().isEmpty())
            throw new InvalidParameterException("the following emails are not well formed: " + invalidEmails.toString());
    }
}
