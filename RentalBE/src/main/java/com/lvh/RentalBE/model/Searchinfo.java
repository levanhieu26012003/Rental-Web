/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lvh.RentalBE.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author levan
 */
@Entity
@Table(name = "searchinfo")
@NamedQueries({
    @NamedQuery(name = "Searchinfo.findAll", query = "SELECT s FROM Searchinfo s"),
    @NamedQuery(name = "Searchinfo.findById", query = "SELECT s FROM Searchinfo s WHERE s.id = :id"),
    @NamedQuery(name = "Searchinfo.findByActive", query = "SELECT s FROM Searchinfo s WHERE s.active = :active"),
    @NamedQuery(name = "Searchinfo.findByCreatedDate", query = "SELECT s FROM Searchinfo s WHERE s.createdDate = :createdDate"),
    @NamedQuery(name = "Searchinfo.findByUpdatedDate", query = "SELECT s FROM Searchinfo s WHERE s.updatedDate = :updatedDate"),
    @NamedQuery(name = "Searchinfo.findByLocation", query = "SELECT s FROM Searchinfo s WHERE s.location = :location"),
    @NamedQuery(name = "Searchinfo.findByMinPrice", query = "SELECT s FROM Searchinfo s WHERE s.minPrice = :minPrice"),
    @NamedQuery(name = "Searchinfo.findByMaxPrice", query = "SELECT s FROM Searchinfo s WHERE s.maxPrice = :maxPrice"),
    @NamedQuery(name = "Searchinfo.findByNumRooms", query = "SELECT s FROM Searchinfo s WHERE s.numRooms = :numRooms")})
public class Searchinfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "active")
    private boolean active;
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @Basic(optional = false)
    @Column(name = "location")
    private String location;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "min_price")
    private BigDecimal minPrice;
    @Column(name = "max_price")
    private BigDecimal maxPrice;
    @Basic(optional = false)
    @Column(name = "num_rooms")
    private int numRooms;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User userId;
    @OneToMany(mappedBy = "searchInfoId")
    private Collection<Comment> commentCollection;

    public Searchinfo() {
    }

    public Searchinfo(Long id) {
        this.id = id;
    }

    public Searchinfo(Long id, boolean active, String location, int numRooms) {
        this.id = id;
        this.active = active;
        this.location = location;
        this.numRooms = numRooms;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getNumRooms() {
        return numRooms;
    }

    public void setNumRooms(int numRooms) {
        this.numRooms = numRooms;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Collection<Comment> getCommentCollection() {
        return commentCollection;
    }

    public void setCommentCollection(Collection<Comment> commentCollection) {
        this.commentCollection = commentCollection;
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
        if (!(object instanceof Searchinfo)) {
            return false;
        }
        Searchinfo other = (Searchinfo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.lvh.pojo.Searchinfo[ id=" + id + " ]";
    }
    
}
