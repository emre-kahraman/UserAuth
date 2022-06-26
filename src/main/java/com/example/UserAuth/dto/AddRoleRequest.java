package com.example.UserAuth.dto;

public class AddRoleRequest {

    private String username;

    private String roleName;

    public AddRoleRequest(String username, String roleName) {
        this.username = username;
        this.roleName = roleName;
    }

    public String getUsername() {
        return username;
    }

    public String getRoleName() {
        return roleName;
    }
}
