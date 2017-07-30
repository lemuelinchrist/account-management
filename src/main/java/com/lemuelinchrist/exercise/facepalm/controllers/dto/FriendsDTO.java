package com.lemuelinchrist.exercise.facepalm.controllers.dto;

import java.util.List;

/**
 * @author Lemuel Cantos
 * @since 30/7/2017
 */
public class FriendsDTO {

    private String success;
    private List<String> friends;
    private Integer count;

    public FriendsDTO(String success, List<String> friends, Integer count) {
        this.success = success;
        this.friends = friends;
        this.count = count;
    }

    public FriendsDTO() {

    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
