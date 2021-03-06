package com.lemuelinchrist.exercise.facepalm.controllers.dto;

import com.lemuelinchrist.exercise.facepalm.exception.InvalidParameterException;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Lemuel Cantos
 * @since 30/7/2017
 */
public class RequestDTOTest {
    @Test
    public void friendPairRequestDTOshouldThrowExceptionWhenInvalidEmail() {
        FriendPairRequestDTO befriend = new FriendPairRequestDTO();
        befriend.setFriends(Arrays.asList("bademail", "anotherBadEmail"));
        assertThatThrownBy(befriend::checkValidity);
        befriend.setFriends(Arrays.asList("asdf@ffd", "proper@yahoo.com"));
        befriend.setFriends(Arrays.asList("nextIs@empty.com", ""));
        assertThatThrownBy(befriend::checkValidity);
        befriend.setFriends(Arrays.asList("", "previous@empty.com"));
        assertThatThrownBy(befriend::checkValidity);
        befriend.setFriends(Arrays.asList("three@mail.com", "emails@gmail.com", "thirdOne@asdf.com"));
        assertThatThrownBy(befriend::checkValidity);

    }

    @Test
    public void requestorTargetDTOShouldThrowExceptionWhenInvalidEmail() {
        RequestorTargetDTO requestorTarget = new RequestorTargetDTO();
        assertThatThrownBy(requestorTarget::checkValidity).isInstanceOf(InvalidParameterException.class);
        requestorTarget.setRequestor("");
        requestorTarget.setTarget("");
        assertThatThrownBy(requestorTarget::checkValidity).isInstanceOf(InvalidParameterException.class);
        requestorTarget.setRequestor("invalidEmail");
        requestorTarget.setRequestor("anot@her");
        assertThatThrownBy(requestorTarget::checkValidity).isInstanceOf(InvalidParameterException.class);
    }

    @Test
    public void properEmailShouldNowThrowException() {
        FriendPairRequestDTO befriend = new FriendPairRequestDTO();
        befriend.setFriends(Arrays.asList("proper@gmail.com", "email@gmail.com"));
    }

}