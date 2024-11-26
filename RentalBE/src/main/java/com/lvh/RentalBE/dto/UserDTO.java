package com.lvh.RentalBE.dto;

import java.util.Collection;
import java.util.List;

public class UserDTO {
    private Long id;
    private String username;
    private String fullname;
    private String email;
    private String avatar;
    public List<UserDTO> getFollower() {
        return follower;
    }
    public List<UserDTO> getFollowing() {
        return following;
    }
    private String userrole;
    private List<UserDTO> follower;
    private List<UserDTO> following;
    private boolean active;

    // Constructors
    public UserDTO() {}

    public UserDTO(Long id, String username, String fullname, String email, String userrole,String avatar, boolean active) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.userrole = userrole;
        this.avatar = avatar;
        this.active = active;
    }


    public void setFollowing(List<UserDTO> following) {
        this.following = following;
    }

    public void setFollower(List<UserDTO> follower) {
        this.follower = follower;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserrole() {
        return userrole;
    }

    public void setUserrole(String userrole) {
        this.userrole = userrole;
    }


    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
