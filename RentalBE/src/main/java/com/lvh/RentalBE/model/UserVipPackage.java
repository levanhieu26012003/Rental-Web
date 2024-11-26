/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lvh.RentalBE.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author levan
 */
@Entity
@Table(name = "user_vip_package")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UserVipPackage.findAll", query = "SELECT u FROM UserVipPackage u"),
        @NamedQuery(name = "UserVipPackage.findByUserId", query = "SELECT u FROM UserVipPackage u WHERE u.userVipPackagePK.userId = :userId"),
        @NamedQuery(name = "UserVipPackage.findByVipPackageId", query = "SELECT u FROM UserVipPackage u WHERE u.userVipPackagePK.vipPackageId = :vipPackageId"),
        @NamedQuery(name = "UserVipPackage.findByDayStart", query = "SELECT u FROM UserVipPackage u WHERE u.dayStart = :dayStart"),
        @NamedQuery(name = "UserVipPackage.findByDayEnd", query = "SELECT u FROM UserVipPackage u WHERE u.dayEnd = :dayEnd")})
public class UserVipPackage implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UserVipPackagePK userVipPackagePK;
    @Column(name = "day_start")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dayStart;
    @Column(name = "day_end")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dayEnd;
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;
    @JoinColumn(name = "vip_package_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Vippackage vippackage;

    public UserVipPackage() {
    }

    public UserVipPackage(UserVipPackagePK userVipPackagePK) {
        this.userVipPackagePK = userVipPackagePK;
    }

    public UserVipPackage(long userId, long vipPackageId) {
        this.userVipPackagePK = new UserVipPackagePK(userId, vipPackageId);
    }

    public UserVipPackagePK getUserVipPackagePK() {
        return userVipPackagePK;
    }

    public void setUserVipPackagePK(UserVipPackagePK userVipPackagePK) {
        this.userVipPackagePK = userVipPackagePK;
    }

    public Date getDayStart() {
        return dayStart;
    }

    public void setDayStart(Date dayStart) {
        this.dayStart = dayStart;
    }

    public Date getDayEnd() {
        return dayEnd;
    }

    public void setDayEnd(Date dayEnd) {
        this.dayEnd = dayEnd;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Vippackage getVippackage() {
        return vippackage;
    }

    public void setVippackage(Vippackage vippackage) {
        this.vippackage = vippackage;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userVipPackagePK != null ? userVipPackagePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserVipPackage)) {
            return false;
        }
        UserVipPackage other = (UserVipPackage) object;
        if ((this.userVipPackagePK == null && other.userVipPackagePK != null) || (this.userVipPackagePK != null && !this.userVipPackagePK.equals(other.userVipPackagePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.lvh.RentalBE.models.UserVipPackage[ userVipPackagePK=" + userVipPackagePK + " ]";
    }

}
