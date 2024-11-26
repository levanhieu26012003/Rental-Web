/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lvh.RentalBE.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author levan
 */
@Entity
@Table(name = "reviews")
@NamedQueries({
    @NamedQuery(name = "Reviews.findAll", query = "SELECT r FROM Reviews r"),
    @NamedQuery(name = "Reviews.findById", query = "SELECT r FROM Reviews r WHERE r.id = :id"),
    @NamedQuery(name = "Reviews.findByRating", query = "SELECT r FROM Reviews r WHERE r.rating = :rating"),
    @NamedQuery(name = "Reviews.findByCreatedDate", query = "SELECT r FROM Reviews r WHERE r.createdDate = :createdDate"),
    @NamedQuery(name = "Reviews.findByUpdatedDate", query = "SELECT r FROM Reviews r WHERE r.updatedDate = :updatedDate")})
public class Reviews implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "rating")
    private Integer rating;
    @Lob
    @Column(name = "comment")
    private String comment;
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @JsonIgnore
    @JoinColumn(name = "host_id", referencedColumnName = "id")
    @ManyToOne
    private User hostId;
    @JsonIgnore
    @JoinColumn(name = "tenant_id", referencedColumnName = "id")
    @ManyToOne
    private User tenantId;

    public Reviews() {
    }

    public Reviews(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public User getHostId() {
        return hostId;
    }

    public void setHostId(User hostId) {
        this.hostId = hostId;
    }

    public User getTenantId() {
        return tenantId;
    }

    public void setTenantId(User tenantId) {
        this.tenantId = tenantId;
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
        if (!(object instanceof Reviews)) {
            return false;
        }
        Reviews other = (Reviews) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
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

    @Override
    public String toString() {
        return "com.lvh.pojo.Reviews[ id=" + id + " ]";
    }
    
}
