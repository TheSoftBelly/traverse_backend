package com.example.userauth.dto;

import com.example.userauth.model.User;

import java.util.List;

public class UserResponse {
    private List<User> users;
    private long totalCount;

    public UserResponse(List<User> users, long totalCount) {
        this.users = users;
        this.totalCount = totalCount;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
