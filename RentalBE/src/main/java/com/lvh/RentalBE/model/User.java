/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lvh.RentalBE.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author levan
 */
@Entity
@Table(name = "user")
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"),
    @NamedQuery(name = "User.findByActive", query = "SELECT u FROM User u WHERE u.active = :active"),
    @NamedQuery(name = "User.findByCreatedDate", query = "SELECT u FROM User u WHERE u.createdDate = :createdDate"),
    @NamedQuery(name = "User.findByUpdatedDate", query = "SELECT u FROM User u WHERE u.updatedDate = :updatedDate"),
    @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username"),
    @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password"),
    @NamedQuery(name = "User.findByAvatar", query = "SELECT u FROM User u WHERE u.avatar = :avatar"),
    @NamedQuery(name = "User.findByUserRole", query = "SELECT u FROM User u WHERE u.userRole = :userRole"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email")})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "active")
    private Boolean active = true;
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @Basic(optional = false)
    @Column(name = "username")
    private String username;
    @Column(name = "fullname")
    private String fullname;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Column(name = "avatar")
    private String avatar;
    @Column(name = "user_role")
    private String userRole;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @JsonIgnore
    @JoinTable(name = "follows", joinColumns = {
        @JoinColumn(name = "follower_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "host_id", referencedColumnName = "id")})
    @ManyToMany(fetch = FetchType.LAZY )
    private Collection<User> userCollection;
    @JsonIgnore
    @ManyToMany(mappedBy = "userCollection", fetch = FetchType.LAZY)
    private Collection<User> userCollection1;
    @OneToMany(mappedBy = "hostId",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<Reviews> reviewsCollection;
    @JsonIgnore
    @OneToMany(mappedBy = "tenantId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<Reviews> reviewsCollection1;
    @OneToMany(mappedBy = "userId",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<Searchinfo> searchinfoCollection;
    @JsonIgnore
    @OneToMany(mappedBy = "userId",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<Comment> commentCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId", fetch = FetchType.LAZY)
    private Collection<Motel> motelCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<UserVipPackage> userVipPackageCollection;
    @Transient
    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public User() {
    }

    public User(Long id) {
        this.id = id;
    }

    public User(Long id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getFullname() {return fullname;}

    public void setFullname(String fullname) {this.fullname = fullname;}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Collection<User> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(Collection<User> userCollection) {
        this.userCollection = userCollection;
    }

    public Collection<User> getUserCollection1() {
        return userCollection1;
    }

    public void setUserCollection1(Collection<User> userCollection1) {
        this.userCollection1 = userCollection1;
    }

    public Collection<Reviews> getReviewsCollection() {
        return reviewsCollection;
    }

    public void setReviewsCollection(Collection<Reviews> reviewsCollection) {
        this.reviewsCollection = reviewsCollection;
    }

    public Collection<Reviews> getReviewsCollection1() {
        return reviewsCollection1;
    }

    public void setReviewsCollection1(Collection<Reviews> reviewsCollection1) {
        this.reviewsCollection1 = reviewsCollection1;
    }

    public Collection<Searchinfo> getSearchinfoCollection() {
        return searchinfoCollection;
    }

    public void setSearchinfoCollection(Collection<Searchinfo> searchinfoCollection) {
        this.searchinfoCollection = searchinfoCollection;
    }

    public Collection<Comment> getCommentCollection() {
        return commentCollection;
    }

    public void setCommentCollection(Collection<Comment> commentCollection) {
        this.commentCollection = commentCollection;
    }

    public Collection<Motel> getMotelCollection() {
        return motelCollection;
    }

    public void setMotelCollection(Collection<Motel> motelCollection) {
        this.motelCollection = motelCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.lvh.pojo.User[ id=" + id + " ]";
    }

    @PrePersist
    protected void onCreate() {
        createdDate = new Date();
        updatedDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = new Date();
    }

    @XmlTransient
    public Collection<UserVipPackage> getUserVipPackageCollection() {
        return userVipPackageCollection;
    }

    public void setUserVipPackageCollection(Collection<UserVipPackage> userVipPackageCollection) {
        this.userVipPackageCollection = userVipPackageCollection;
    }
}
