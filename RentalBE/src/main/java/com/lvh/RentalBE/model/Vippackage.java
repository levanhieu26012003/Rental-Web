/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lvh.RentalBE.model;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author levan
 */
@Entity
@Table(name = "vippackage")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Vippackage.findAll", query = "SELECT v FROM Vippackage v"),
        @NamedQuery(name = "Vippackage.findById", query = "SELECT v FROM Vippackage v WHERE v.id = :id"),
        @NamedQuery(name = "Vippackage.findByType", query = "SELECT v FROM Vippackage v WHERE v.type = :type"),
        @NamedQuery(name = "Vippackage.findByPrice", query = "SELECT v FROM Vippackage v WHERE v.price = :price"),
        @NamedQuery(name = "Vippackage.findByLimitTime", query = "SELECT v FROM Vippackage v WHERE v.limitTime = :limitTime"),
        @NamedQuery(name = "Vippackage.findByUpgradeTime", query = "SELECT v FROM Vippackage v WHERE v.upgradeTime = :upgradeTime")})
public class Vippackage implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @Column(name = "price")
    private double price;
    @Column(name = "limit_time")
    private Integer limitTime;
    @Column(name = "upgrade_time")
    private Integer upgradeTime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vippackage")
    private Collection<UserVipPackage> userVipPackageCollection;

    public Vippackage() {
    }

    public Vippackage(Long id) {
        this.id = id;
    }

    public Vippackage(Long id, String type, double price) {
        this.id = id;
        this.type = type;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(Integer limitTime) {
        this.limitTime = limitTime;
    }

    public Integer getUpgradeTime() {
        return upgradeTime;
    }

    public void setUpgradeTime(Integer upgradeTime) {
        this.upgradeTime = upgradeTime;
    }

    @XmlTransient
    public Collection<UserVipPackage> getUserVipPackageCollection() {
        return userVipPackageCollection;
    }

    public void setUserVipPackageCollection(Collection<UserVipPackage> userVipPackageCollection) {
        this.userVipPackageCollection = userVipPackageCollection;
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
        if (!(object instanceof Vippackage)) {
            return false;
        }
        Vippackage other = (Vippackage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.lvh.RentalBE.models.Vippackage[ id=" + id + " ]";
    }

}
