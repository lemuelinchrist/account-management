package com.lemuelinchrist.exercise.facepalm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * An Account represents a Facepalm user account that is able to make friends, subscribe to updates, and block updates
 * similar to a Facebook application.
 *
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
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Account> friends;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Account> subscribers;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Account> blockedAccounts;


    @JsonIgnore
    @ManyToMany(mappedBy = "blockedAccounts", fetch = FetchType.LAZY)
    private Set<Account> blockers;

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

    public void addSubscriber(Account account) {
        if (subscribers == null) {
            subscribers = new HashSet<>();
        }
        subscribers.add(account);

    }

    public void addBlockedAccount(Account account) {
        if (blockedAccounts == null) {
            blockedAccounts = new HashSet<>();
        }
        blockedAccounts.add(account);

    }

    public void addBlocker(Account account) {
        if (blockers == null) {
            blockers = new HashSet<>();
        }
        blockers.add(account);

    }

    public Set<Account> getFriends() {
        return friends;
    }

    public void setFriends(Set<Account> friends) {
        this.friends = friends;
    }

    public Set<Account> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<Account> subscribers) {
        this.subscribers = subscribers;
    }

    public Set<Account> getBlockedAccounts() {
        return blockedAccounts;
    }

    public void setBlockedAccounts(Set<Account> blockedAccounts) {
        this.blockedAccounts = blockedAccounts;
    }

    public Set<Account> getBlockers() {
        return blockers;
    }

    public void setBlockers(Set<Account> blockers) {
        this.blockers = blockers;
    }

}
