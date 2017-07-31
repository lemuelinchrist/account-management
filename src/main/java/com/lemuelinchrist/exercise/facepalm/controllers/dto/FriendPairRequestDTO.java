package com.lemuelinchrist.exercise.facepalm.controllers.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lemuelinchrist.exercise.facepalm.exception.InvalidParameterException;

import java.util.Arrays;
import java.util.List;

/**
 * Convenient DTO object for JSON conversion of requests
 *
 * @author Lemuel Cantos
 * @since 30/7/2017
 */
public class FriendPairRequestDTO extends RequestDTO {
    private List<String> friends;

    public FriendPairRequestDTO() {

    }

    public FriendPairRequestDTO(String firstEmail, String secondEmail) {
        friends = Arrays.asList(firstEmail, secondEmail);
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    @JsonIgnore
    public String getFirstFriend() {
        return friends.get(0);
    }

    @JsonIgnore
    public String getSecondFriend() {
        return friends.get(1);
    }

    /**
     * Ensures that this DTO Object will have exactly two emails and the emails are not empty and that they are properly
     * formed emails.
     *
     * @throws InvalidParameterException Thrown if the emails in friends list do not meet the expected criteria.
     */
    public void checkValidity() throws InvalidParameterException {
        if (friends.size() != 2) throw new InvalidParameterException("You should only specify two emails");
        final StringBuilder invalidEmails = new StringBuilder();
        for (String email : friends) {
            if (email.isEmpty()) throw new InvalidParameterException("One of the emails is empty");
            if (!isValidEmail(email)) {
                invalidEmails.append(email);
                invalidEmails.append(", ");
            }
        }

        if (!invalidEmails.toString().isEmpty())
            throw new InvalidParameterException("the following emails are not well formed: " + invalidEmails.toString());
    }

}
