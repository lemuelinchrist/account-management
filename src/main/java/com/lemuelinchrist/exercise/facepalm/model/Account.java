package com.lemuelinchrist.exercise.facepalm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Lemuel Cantos
 * @since 29/7/2017
 */
@Entity
public class Account {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    @NotEmpty
    @Email
    private String email;

    @JsonIgnore //This needs to be excluded during json serialization because it will create an endless recursion
    @Column(unique = true)
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Account> friends;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;

        Account account = (Account) o;

        return email.equals(account.email);
    }


    public void addFriend(Account account) {
        if (friends == null) {
            friends = new HashSet<>();
        }
        friends.add(account);

    }

    public Set<Account> getFriends() {
        return friends;
    }

    public void setFriends(Set<Account> friends) {
        this.friends = friends;
    }
}
